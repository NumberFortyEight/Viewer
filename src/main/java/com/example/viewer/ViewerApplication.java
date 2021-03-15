package com.example.viewer;

import com.example.viewer.models.Node;
import com.example.viewer.util.PathHelper;
import com.example.viewer.util.jgit.JgitCommits;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.List;

//@SpringBootApplication
public class ViewerApplication {

	public static void main(String[] args) throws IOException, GitAPIException {
		//SpringApplication.run(ViewerApplication.class, args);

		List<RevCommit> allCommits = new JgitCommits().getInfo(PathHelper.getRelativePath("/root/Repo.git")).getAllCommits();

		System.out.println(("s1dsdsd/sdsdsd/dsdsds/sdsdsd/sdsd").split("/")[0]);

	}

	public void dependencySetter(String path, Node node, RevCommit revCommit){
		String normalizePath = PathHelper.getRelativePath(path);
		String[] split = normalizePath.split("/");
		if (node.getName() == null || !node.getName().equals(split[0])) {
			node.setName(split[0]);
			node.setRevCommit(revCommit);
			if (split.length>=2) {
				Node childNode = new Node();
				childNode.setName(split[1]);
				childNode.setParentNode(node);
				node.setChildNodeList(List.of(childNode));
			}
		} else if(node.getName().equals(split[0])){
			dependencySetter(PathHelper.skip(path, 1), node.);
		}
	}

}
