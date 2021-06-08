package com.example.viewer.services.nodes;

import com.example.viewer.dataClasses.Node;
import com.example.viewer.exceptions.JGit.JGitCommitInfoException;
import com.example.viewer.services.interfaces.NodeExplorerService;
import com.example.viewer.services.interfaces.NodeTreeFinderService;
import com.example.viewer.services.JGitFactoryService;
import com.example.viewer.util.PathHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NodeExplorerServiceImpl implements NodeExplorerService {

    public final NodeTreeFinderService nodeTreeFinderService;
    private final JGitFactoryService jGitFactoryService;

    @Override
    public RevCommit findCommitInNodeByPath(String username, String fullPath, Map<String, Node> userAndNodeTree) {
        log.debug( "find commit in node {}  with nickname {}",fullPath, username);
        String repositoryWithWorkPath = PathHelper.skip(fullPath, 1);
        Optional<Node> optionalNode = nodeTreeFinderService.getOptionalNodeTreeByUsername(username, userAndNodeTree);
        try {
            RevCommit firstRevCommit = jGitFactoryService.getCommitInfo(fullPath).getFirstRevCommit();
            if (optionalNode.isPresent()) {
                Node node = optionalNode.get();
                if (!node.getName().equals(PathHelper.limit(repositoryWithWorkPath, 1))) {
                    return firstRevCommit;
                }
                return getTargetCommitInNode(node, repositoryWithWorkPath).orElse(firstRevCommit);
            } else {
                return firstRevCommit;
            }
        } catch (GitAPIException e) {
            log.warn("fail on find commit from path {}", fullPath);
            throw new JGitCommitInfoException("Exception of load commit", e);
        }
    }

    private Optional<RevCommit> getTargetCommitInNode(Node node, String path) {
        String[] pathNuggets = path.split("/");

        if (pathNuggets.length > 1) {
            Optional<Node> commonChild = node.getChildNodeList()
                    .stream()
                    .filter(childNode -> childNode.getName().equals(pathNuggets[1]))
                    .findFirst();
            if (commonChild.isPresent()) {
                return getTargetCommitInNode(commonChild.get(), PathHelper.skip(path, 1));
            }
        }
        if (node.getName().equals(pathNuggets[0])) {
            return Optional.ofNullable(node.getRevCommit());
        } else {
            return Optional.empty();
        }
    }

}
