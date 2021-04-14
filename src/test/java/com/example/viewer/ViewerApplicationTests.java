package com.example.viewer;

import com.example.viewer.services.CommitServiceImpl;
import com.example.viewer.services.JGitServiceImpl;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

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
