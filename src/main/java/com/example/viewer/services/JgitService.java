package com.example.viewer.services;

import com.example.viewer.enums.ContentType;
import com.example.viewer.models.ContentModel;
import com.example.viewer.models.Node;
import com.example.viewer.services.jgit.JGitObjectProducer;
import com.example.viewer.services.jgit.JgitCommits;
import com.example.viewer.util.PathHelper;
import org.eclipse.jgit.revwalk.RevCommit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Optional;

@Service
public class JgitService {
    private static final Logger LOGGER = LoggerFactory.getLogger(NodeCreateService.class);

    public ContentModel loadFileOrDirs(String user, String fullPath, Map<String, Node> userAndNodeTree) {
        try {
            RevCommit revCommit = findCommit(user, fullPath, userAndNodeTree);
            JGitObjectProducer jGitObjectProducer = new JgitCommits().getJGitObject(revCommit, fullPath);

            if (jGitObjectProducer.isThisExist()) {
                if (jGitObjectProducer.isFile()) {
                    if (jGitObjectProducer.isImage()) {
                        return new ContentModel(ContentType.IMAGE, jGitObjectProducer.loadFile());
                    }
                    return new ContentModel(ContentType.BYTES, jGitObjectProducer.loadFile());
                } else {
                    return new ContentModel(ContentType.JSON, jGitObjectProducer.getDirs());
                }
            } else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Object not found" + fullPath);
        } catch (Exception e) {
            LOGGER.warn("Content load fail on path: " + fullPath, e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Content load fail on path: " + fullPath);
        }
    }

    private RevCommit findCommit(String user, String fullPath, Map<String, Node> userAndNodeTree) {
        RevCommit firstRevCommit = new JgitCommits().getInfo(PathHelper.getAbsolutePath(PathHelper.limit(fullPath, 2))).findFirstRevCommit();
        Optional<Node> optionalNode = FindAnExistingNode(user, userAndNodeTree);
        String repositoryWithWorkPath = PathHelper.skip(fullPath, 1);

        if (optionalNode.isPresent()) {
            return getTargetCommitInNode(repositoryWithWorkPath, optionalNode.get()).orElse(firstRevCommit);
        } else {
            return firstRevCommit;
        }
    }

    private Optional<RevCommit> getTargetCommitInNode(String repositoryWithWorkPath, Node node) {
        Optional<Node> commonChild = findCommonChild(node, repositoryWithWorkPath);
        return Optional.ofNullable(commonChild.get().getRevCommit());
    }

    private Optional<Node> findCommonChild(Node node, String path) {
        String[] pathNuggets = path.split("/");

        if (pathNuggets.length > 1) {
            Optional<Node> commonChild = node.getChildNodeList()
                    .stream()
                    .filter(childNode -> childNode.getName().equals(pathNuggets[1]))
                    .findFirst();
            if (commonChild.isPresent()) {
                return findCommonChild(commonChild.get(), PathHelper.skip(path, 1));
            } else {
                if (node.getName().equals(pathNuggets[0])) {
                    return Optional.of(node);
                } else {
                    return Optional.empty();
                }
            }
        } else {
            if (node.getName().equals(pathNuggets[0])) {
                return Optional.of(node);
            } else {
                return Optional.empty();
            }
        }

    }

//    private RevCommit getTargetCommitInNode(String repositoryWithWorkPath, Node node) {
//        try {
//            String[] pathNuggets = PathHelper.getRelativePath(repositoryWithWorkPath).split("/");
//            if (node.getName().equals(pathNuggets[0])) {
//                Optional<Node> optionalCommonChild = node.getChildNodeList().stream().filter(childNode -> childNode.getName().equals(pathNuggets[1])).findFirst();
//                return optionalCommonChild.isPresent() ? getTargetCommitInNode(PathHelper.skip(repositoryWithWorkPath, 1), optionalCommonChild.get()) : node.getRevCommit();
//            } else {
//                LOGGER.debug("choose Node - " + node.getName());
//                return node.getRevCommit();
//            }
//        } catch (NullPointerException | ArrayIndexOutOfBoundsException | StringIndexOutOfBoundsException e)
//
//    }

    private Optional<Node> FindAnExistingNode(String user, Map<String, Node> userAndNodeTree) {
        return userAndNodeTree.containsKey(user) ? Optional.ofNullable(userAndNodeTree.get(user)) : Optional.empty();
    }
}
