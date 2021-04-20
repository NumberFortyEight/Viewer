package com.example.viewer;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;

@SpringBootTest
class ViewerApplicationTests {

	@Test
	void contextLoads() throws IOException, GitAPIException {
		/*String nickname = "Дмитрий Власенко";
		String gitname = "Students git" + ".git";

		Git.cloneRepository()
				.setBare(true)
				.setURI("https://github.com/DmitryqV/students-git-project")
				.setDirectory(new File("C:/repositories/"+nickname+"/"+gitname))
				.call();*/

	}
}
