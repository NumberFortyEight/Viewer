package com.example.viewer.services.nodes;

import com.example.viewer.dataClasses.Node;
import com.example.viewer.exceptions.JGit.JGitCommitInfoException;
import com.example.viewer.services.interfaces.NodeExplorerService;
import com.example.viewer.services.interfaces.NodeTreeFinderService;
import com.example.viewer.services.jgit.JGitCommitInfo;
import com.example.viewer.util.PathHelper;
import lombok.RequiredArgsConstructor;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NodeExplorerServiceImpl implements NodeExplorerService {

    public final NodeTreeFinderService nodeTreeFinderService;
    private final ApplicationContext appContext;

    @Override
    public RevCommit findCommitInNodeTreeByPath(String username, String fullPath, Map<String, Node> userAndNodeTree) {
        JGitCommitInfo jGitCommitInfo = appContext.getBean(JGitCommitInfo.class);
        Optional<Node> optionalNode = nodeTreeFinderService.getOptionalNodeTreeByUsername(username, userAndNodeTree);
        String repositoryWithWorkPath = PathHelper.skip(fullPath, 1);
        try {
            jGitCommitInfo.setGitByPath(fullPath);
            RevCommit firstRevCommit = jGitCommitInfo.findFirstRevCommit();
            if (optionalNode.isPresent()) {
                return getTargetCommitInNode(optionalNode.get(), repositoryWithWorkPath).orElse(firstRevCommit);
            } else {
                return firstRevCommit;
            }
        } catch (IOException | GitAPIException e) {
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