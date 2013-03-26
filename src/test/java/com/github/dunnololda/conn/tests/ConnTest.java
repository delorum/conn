package com.github.dunnololda.conn.tests;

import com.github.dunnololda.conn.Conn;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class ConnTest {
    @Test public void testConn() throws Exception {
        Conn conn = new Conn();
        conn.executeGet("http://google.com");
        assertTrue(200 == conn.currentStatusCode);
        assertTrue("HTTP/1.1 200 OK".equals(conn.currentTextStatus));
    }
}
