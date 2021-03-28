package com.example.viewer.services;

import com.example.viewer.models.Node;
import com.example.viewer.services.interfaces.NodeExplorerService;
import com.example.viewer.services.interfaces.NodeTreeFinderService;
import com.example.viewer.services.jgit.JGitScope;
import com.example.viewer.util.PathHelper;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class NodeExplorerServiceImpl implements NodeExplorerService {

    public final NodeTreeFinderService nodeTreeFinderService;

    public NodeExplorerServiceImpl(NodeTreeFinderService nodeTreeFinderService) {
        this.nodeTreeFinderService = nodeTreeFinderService;
    }

    @Override
    public RevCommit findCommitInNodeTreeByPath(String username, String fullPath, Map<String, Node> userAndNodeTree) {
        RevCommit firstRevCommit = new JGitScope(fullPath).getCommitInfo().findFirstRevCommit();
        Optional<Node> optionalNode = nodeTreeFinderService.getOptionalNodeTreeByUsername(username, userAndNodeTree);
        String repositoryWithWorkPath = PathHelper.skip(fullPath, 1);

        if (optionalNode.isPresent()) {
            return getTargetCommitInNode(optionalNode.get(), repositoryWithWorkPath).orElse(firstRevCommit);
        } else {
            return firstRevCommit;
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
