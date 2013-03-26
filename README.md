conn
====

Simple java http client. Can execute get and post requests, receive text and binary answers from web servers, set custom headers and more.

Usage
=====

Maven
-----

Add to your `<repositories>` section:

      <repository>
          <id>dunnololda's maven repo</id>
          <url>https://raw.github.com/dunnololda/mvn-repo/master</url>
      </repository>
      
Add to your `<dependencies>` section:

      <dependency>
          <groupId>com.github.dunnololda</groupId>
          <artifactId>conn</artifactId>
          <version>1.0</version>
      </dependency>

SBT
---

Add to resolvers:

    resolvers += "dunnololda's repo" at "https://raw.github.com/dunnololda/mvn-repo/"
    
Add to dependencies:

    libraryDependencies ++= Seq(
      // ...
      "com.github.dunnololda" % "conn" % "1.0",
      // ...
    )

Code
----

Create instance of `com.github.dunnololda.conn.Conn` class:

    import com.github.dunnololda.conn.Conn;
    // ...
    Conn conn = new Conn();
    
Call methods `executeGet()`, `executePost()` with url provided as String argument.

`executePost()` accepts additional boolean argument `isMultiPartData` (default is `false`. Also `executeMultipartPost()` method is available).

Add headers to your requests using `addHeader(String key, String value)` method.

Add post data using `addPostData()` method. Post data must be provided as JSONObject.

If you want to execute multipart post, use `addMultipartPostData()`.

Answer to request can be received using this public field in Conn instance:

    String conn.currentUrl
    int conn.currentStatusCode
    String conn.currentTextStatus
    HashMap<String, String> conn.currentHeaders
    StringBuilder conn.currentCookies
    String conn.currentHtml
    
If you want to retrieve binary data use `getBinaryData(OutputStream os, String link)` method. Binary object then can be retrieved through `OutputStream os`.
