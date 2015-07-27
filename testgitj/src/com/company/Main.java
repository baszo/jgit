package com.company;


import java.io.File;
import java.io.IOException;
import java.sql.Ref;
import java.util.Set;

import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.*;
import org.eclipse.jgit.api.CreateBranchCommand.SetupUpstreamMode;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Repository;

import org.eclipse.jgit.transport.RefSpec;
import org.junit.Before;
import org.junit.Test;

class TestJGit {

    private String localPath, remotePath;
    private Repository localRepo;
    private Git git;

    @Before
    public void init() throws IOException {
        localPath = "/home/andrzej2/repo/jgit";
        remotePath = "git@github.com:baszo/jgit.git";
        localRepo = new FileRepository(localPath + "/.git");
        git = new Git(localRepo);
    }

    @Test
    public void testCreate() throws IOException {
        Repository newRepo = new FileRepository(localPath + ".git");
        newRepo.create();
    }

    @Test
    public void testReset() throws IOException, GitAPIException {
        git.fetch().setRefSpecs(new RefSpec("refs/heads/*:refs/remotes/origin/*")).call();
//        git.reset().setMode(ResetCommand.ResetType.HARD).setRef(Constants.HEAD).call();
        git.reset().setMode(ResetCommand.ResetType.HARD).setRef("origin/master").call();
//        git.reset().setMode(ResetCommand.ResetType.MIXED).call();
    }

    @Test
    public void testClone() throws IOException, GitAPIException {
        Git.cloneRepository().setURI(remotePath)
                .setDirectory(new File(localPath)).call();
    }

    @Test
    public void testAdd() throws IOException, GitAPIException {
        File myfile = new File(localPath + "/myfile2");
        myfile.createNewFile();
        git.add().addFilepattern("myfile2").call();
    }

    @Test
    public void testCommit() throws IOException, GitAPIException,
            JGitInternalException {
        git.commit().setMessage("Added myfile2").call();
    }

    @Test
    public void testPush() throws IOException, JGitInternalException,
            GitAPIException {
        git.push().call();
    }

    @Test
    public void testTrackMaster() throws IOException, JGitInternalException,
            GitAPIException {
        git.branchCreate().setName("master")
                .setUpstreamMode(SetupUpstreamMode.SET_UPSTREAM)
                .setStartPoint("origin/master").setForce(true).call();
    }

    @Test
    public void testPull() throws IOException, GitAPIException {
        git.pull().call();
    }

    public void testRemove() throws GitAPIException {
        Set<String> removed = new Git(localRepo).clean().setCleanDirectories(true).call();
        for(String item : removed) {
            System.out.println("Removed: " + item);
        }
    }

    public void testFetch() throws GitAPIException {
        git.fetch().setRemote(localPath)
                .setRefSpecs(new RefSpec("refs/heads/*:refs/heads/*"))
                .call();
    }
}

public class Main {

    public static void main(String[] args) {
	// write your code here
        TestJGit cos = new TestJGit();
        try {
            cos.init();
//            cos.testAdd();
//            cos.testCommit();
//            cos.testPush();
//            cos.testReset();

//            cos.testRemove();
//            cos.testFetch();
            cos.testReset();
//            cos.testPull();
        }
        catch(Exception e){
            System.out.println(e.toString());

        }
    }
}
