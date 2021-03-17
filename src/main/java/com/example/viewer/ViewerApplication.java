package com.example.viewer;

import com.example.viewer.models.Node;
import com.example.viewer.services.JgitService;
import com.example.viewer.services.NodeCreateService;
import com.example.viewer.util.PathHelper;
import com.example.viewer.util.jgit.GetCommitInfo;
import com.example.viewer.util.jgit.JgitCommits;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@SpringBootApplication
public class ViewerApplication {

	public static void main(String[] args) throws IOException, GitAPIException {
		//SpringApplication.run(ViewerApplication.class, args);

		List<RevCommit> allCommits = new JgitCommits().getInfo(PathHelper.getRelativePath("/Student1/TestRepository.git/")).getAllCommits();
		Map<String, Node> userAndNodeTree = new HashMap<>();

		NodeCreateService nodeCreateService = new NodeCreateService();

		nodeCreateService.createNodeHierarchy("aaa", PathHelper.getAbsolutePath("/Student1/Task1.git/"), allCommits.stream().findFirst().get().getCommitTime() , userAndNodeTree);

//		JgitService jgitService = new JgitService();
//		RevCommit revCommit = jgitService.targetCommit("/TestRepository.git/html/1111.html", userAndNodeTree.get("aaa"));
//		System.out.println(revCommit);

		System.out.println(userAndNodeTree);
	}

}
