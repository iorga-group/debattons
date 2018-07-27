package com.iorga.debattons.apiserver.user;

import com.iorga.debattons.apiserver.util.GraphUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.Optional;

@Service
public class UserService {
  public static final int SALT_LENGTH = 66;
  public static final int PASSWORD_ITERATIONS = 1000;
  public static final int PASSWORD_LENGTH = 66;

  @Autowired
  private GraphUtils graphUtils;


  public User create(User user) throws Exception {
    // Must first create the person and then create its wishList
    String password = user.getPassword();
    if (StringUtils.isNotBlank(password)) {
      final byte[] salt = new byte[SALT_LENGTH];
      final byte[] passwordHash = createSaltThenComputePasswordHash(salt, password);

      return graphUtils.doInGraphTransaction(graph -> {
        Vertex reactionVertex = graph.addVertex(
          T.label, "User",
          "login", user.getLogin(), // FIXME assert that this login is unique
          "email", user.getEmail(),
          "passwordHash", passwordHash,
          "salt", salt,
          "creationDate", new Date());
        graphUtils.getRoot(graph).addEdge("created", reactionVertex);
        user.setId(graphUtils.getStringVertexId(reactionVertex, graph));
        return user;
      });
    } else {
      throw new IllegalArgumentException("The given user must define a password");
    }
  }

  public Optional<User> findUserByLogin(String login) throws Exception {
    return graphUtils.doInGraphTransaction(graph -> {
      return findUserTraversalByLogin(login, graph)
        .map(vertexTraverser -> {
          try {
            return User.fromVertex(vertexTraverser.get(), graphUtils);
          } catch (IOException e) {
            throw new RuntimeException("Problem while creating the User from Vertex", e);
          }
        }).tryNext();
    });
  }

  public GraphTraversal<Vertex, Vertex> findUserTraversalByLogin(String login, Graph graph) {
    return graphUtils.getRootTraversal(graph)
      .out("created")
      .hasLabel("User")
      .has("login", login);
  }

  protected static byte[] createSaltThenComputePasswordHash(byte[] salt, String password) throws InvalidKeySpecException, NoSuchAlgorithmException {
    byte[] passwordHash;
    // computing password hash thanks to:
    // - http://security.stackexchange.com/a/4801/18432
    // - http://howtodoinjava.com/security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/
    // - http://stackoverflow.com/a/27928435/535203
    new SecureRandom().nextBytes(salt); // not using getInstanceStrong() because it was too long and useless according to https://tersesystems.com/2015/12/17/the-right-way-to-use-securerandom/

    passwordHash = computePasswordHash(password, salt);
    return passwordHash;
  }

  public static byte[] computePasswordHash(String password, byte[] salt) throws InvalidKeySpecException, NoSuchAlgorithmException {
    byte[] passwordHash;
    PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, PASSWORD_ITERATIONS, PASSWORD_LENGTH * 8);
    passwordHash = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(keySpec).getEncoded();
    return passwordHash;
  }
}
