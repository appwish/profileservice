package io.appwish.profileservice.repository.impl;

/**
 * Contains queries to execute on Postgres.
 *
 * I'm not sure what's the best practice for storing String SQLs, so for now it'll stay here.
 */
public enum Query {
  FIND_ALL_PROFILE("SELECT * FROM profiles"),
  CREATE_PROFILE_TABLE(
    "CREATE TABLE IF NOT EXISTS profiles("
      + "id serial PRIMARY KEY, "
      + "bio VARCHAR (255) NOT NULL, "
      + "cover_image_url VARCHAR (255), "
      + "github_username VARCHAR (50) NOT NULL);");

  private final String sql;

  Query(final String sql) {
    this.sql = sql;
  }

  public String sql() {
    return sql;
  }
}
