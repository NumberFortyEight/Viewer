package com.example.viewer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ViewerApplication {

	public static void main(String[] args){
		SpringApplication.run(ViewerApplication.class, args);

		//List<RevCommit> allCommits = new JgitCommits().getInfo(PathHelper.getRelativePath("/Student1/TestRepository.git/")).getAllCommits();
		//Map<String, Node> userAndNodeTree = new HashMap<>();

		//NodeTreeService nodeCreateService = new NodeTreeService();

		//nodeCreateService.createNodeHierarchy("aaa", PathHelper.getAbsolutePath("/Student1/Task1.git/"), allCommits.stream().findFirst().get().getCommitTime() , userAndNodeTree);

//		JGitService jgitService = new JGitService();
//		RevCommit revCommit = jgitService.targetCommit("/TestRepository.git/html/1111.html", userAndNodeTree.get("aaa"));
//		System.out.println(revCommit);

//		System.out.println(userAndNodeTree);
	}

}
