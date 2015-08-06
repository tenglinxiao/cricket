package com.dianping.cricket.scheduler.rest;

import com.dianping.cricket.scheduler.SchedulerConf;
import com.dianping.cricket.scheduler.rest.util.ResultWrapper;
import com.dianping.cricket.scheduler.rest.util.ResultWrapper.JsonResult;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

/**
 * Created by tenglinxiao on 6/8/15.
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class SchedulerServerInfo {
    private static final String PATH = "path";
    private static final String TREE = "tree";
    private static final String NAME = "name";
    private static final String CHILDREN = "children";

    // Logger object.
    private static Logger logger = Logger.getLogger(SchedulerServerInfo.class);

    // Root path for shell jobs.
    private java.nio.file.Path root = Paths.get(SchedulerConf.getConf().getJobShells());

    @GET
    @Path("readDir")
    public JsonResult readDir(@QueryParam("path") @DefaultValue("") String path) {
        java.nio.file.Path targetPath = Paths.get(path);
        if (targetPath.isAbsolute()) {
            if (!targetPath.startsWith(root)) {
                return ResultWrapper.wrap(Response.Status.FORBIDDEN, "Not allowed to access path out of [" + path + "]!");
            }
        } else {
            targetPath = root.resolve(targetPath);
        }

        if (!Files.exists(targetPath)) {
            return ResultWrapper.wrap(Response.Status.NOT_FOUND, "Path [" + targetPath + "] does NOT exist!");
        }

        if (!targetPath.toFile().isDirectory()) {
            return ResultWrapper.wrap(targetPath.toFile().getAbsolutePath());
        }

        final Map<String, Object> tree = new HashMap<String, Object>();
        tree.put(PATH, targetPath.toFile().getAbsolutePath());

        try {
            Files.walkFileTree(targetPath, new FileVisitor<java.nio.file.Path>() {
                private Stack<Map<String, Object>> stack = new Stack<Map<String, Object>>();
                private Map current = null;

                @Override
                public FileVisitResult preVisitDirectory(java.nio.file.Path dir, BasicFileAttributes attrs) throws IOException {
                    this.current = new HashMap<String, Object>();
                    this.current.put(NAME, dir.toFile().getName());
                    this.current.put(CHILDREN, new ArrayList<Object>());
                    stack.push(this.current);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(java.nio.file.Path file, BasicFileAttributes attrs) throws IOException {
                    ((List)this.current.get(CHILDREN)).add(file.toFile().getName());
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(java.nio.file.Path file, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(java.nio.file.Path dir, IOException exc) throws IOException {
                    Map<String, Object> directory = stack.pop();

                    if (stack.size() == 0) {
                        tree.put(TREE, directory);
                    } else {
                        this.current = stack.get(stack.size() - 1);
                        ((List) this.current.get(CHILDREN)).add(directory);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Error when get directory tree: " + e.getMessage());
            return ResultWrapper.wrap(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return ResultWrapper.wrap(tree);
    }
}
