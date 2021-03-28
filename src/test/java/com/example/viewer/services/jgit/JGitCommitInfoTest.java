package com.example.viewer.services.jgit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JGitCommitInfoTest {

    @Test
    void getDiffOrNull() {
        String diffOrNull = new JGitScope("/st1/task1.git/").getCommitInfo().getDiffOrNull(1616418953);
        System.out.println(diffOrNull);
    }
}