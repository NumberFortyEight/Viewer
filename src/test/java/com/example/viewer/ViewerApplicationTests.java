package com.example.viewer;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class ViewerApplicationTests {

	@Test
	void contextLoads() throws IOException, GitAPIException {
//		Git git = Git.cloneRepository().setURI("https://github.com/PunkandClown/NormalRep.git").call();
//		Repository repository = git.getRepository();
//		System.out.println(repository);
		//TreeWalk treeWalk = new TreeWalk(repository);
		//while (treeWalk.next()) {
		//	System.out.println(treeWalk.getPathString());
		//}
	}

}
