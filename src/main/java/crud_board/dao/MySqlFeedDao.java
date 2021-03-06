package crud_board.dao;

import com.mysql.cj.x.protobuf.MysqlxPrepare;
import crud_board.vo.Feed;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class MySqlFeedDao implements FeedDao {

    private static final Logger LOG = Logger.getGlobal();

    public DataSource ds;

    public void setDataSource(DataSource ds) {
        this.ds = ds;
    }

    public List<Feed> selectList() throws Exception {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = ds.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT PNO, TITLE, WRITER, CRE_DATE, LIKES, VIEWS FROM FEEDS ORDER BY PNO DESC");

            ArrayList<Feed> feeds = new ArrayList<>();

            while(rs.next()) {
                feeds.add(new Feed()
                        .setNo(rs.getInt("PNO"))
                        .setTitle(rs.getString("TITLE"))
                        .setWriter(rs.getString("WRITER"))
                        .setCreatedDate(rs.getDate("CRE_DATE"))
                        .setLikes(rs.getInt("LIKES"))
                        .setViews(rs.getInt("VIEWS"))
                );
            }

            LOG.info("[feedDAO.selectList() success]");
            return feeds;
        } catch (Exception e) {
            LOG.severe("[feedDAO.selectList failed] " + e.getMessage());
            throw e;
        }
        finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
    }

    public Feed selectOne(int no) throws Exception {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        Feed feed = null;

        try {
            conn = ds.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT PNO, TITLE, CONTENT, WRITER, CRE_DATE, LIKES, VIEWS FROM FEEDS WHERE PNO=" + no);

            if (rs.next()) {
                feed = new Feed()
                        .setNo(rs.getInt("PNO"))
                        .setTitle(rs.getString("TITLE"))
                        .setWriter(rs.getString("WRITER"))
                        .setCreatedDate(rs.getDate("CRE_DATE"))
                        .setContent(rs.getString("CONTENT"))
                        .setLikes(rs.getInt("LIKES"))
                        .setViews(rs.getInt("VIEWS"));
            }

            return feed;
        } catch (Exception e) { throw e; }
        finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
    }

    public int insert(Feed feed, HttpSession session) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            String writer = "";

            if (feed.getWriter() != null) {
                writer = feed.getWriter();
            } else {
                writer = (String) session.getAttribute("loginUser");
            }

            conn = ds.getConnection();
            stmt = conn.prepareStatement("INSERT INTO FEEDS (TITLE, CONTENT, WRITER, CRE_DATE) " +
                    "VALUES (?, ?, ?, NOW())");
            stmt.setString(1, feed.getTitle());
            stmt.setString(2, feed.getContent().replace("\r\n", "<br>"));
            stmt.setString(3, writer);
            stmt.executeUpdate();

        } catch (Exception e) {}
        finally {
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
            return -1;
        }
    }

    // overloading
    public int insert(Feed feed) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = ds.getConnection();
            stmt = conn.prepareStatement("INSERT INTO FEEDS (TITLE, CONTENT, WRITER, CRE_DATE) " +
                    "VALUES (?, ?, ?, NOW())");
            stmt.setString(1, feed.getTitle());
            stmt.setString(2, feed.getContent().replace("\r\n", "<br>"));
            stmt.setString(3, feed.getWriter());
            return stmt.executeUpdate();

        } catch (Exception e) {}
        finally {
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
            return -1;
        }
    }

    public int delete(int no) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = ds.getConnection();
            stmt = conn.prepareStatement("DELETE FROM FEEDS WHERE PNO=?");
            stmt.setInt(1, no);
            stmt.executeUpdate();
        } catch (Exception e) { throw e; }
        finally {
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
            return -1;
        }
    }

    public int deleteAll() throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = ds.getConnection();
            pstmt = conn.prepareStatement("DELETE FROM FEEDS WHERE PNO >= 0");
            return pstmt.executeUpdate();
        } catch (Exception e) { throw e; }
        finally {
            try { if (pstmt != null) pstmt.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
            return -1;
        }
    }

    public int update(Feed feed) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = ds.getConnection();
            stmt = conn.prepareStatement("UPDATE FEEDS SET TITLE=?, CONTENT=?, LIKES=?, VIEWS=? WHERE PNO=?");
            stmt.setString(1, feed.getTitle());
            stmt.setString(2, feed.getContent());
            stmt.setInt(3, feed.getLikes());
            stmt.setInt(4, feed.getViews());
            stmt.setInt(5, feed.getNo());
            stmt.executeUpdate();

        } catch (Exception e) { throw e; }
        finally {
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
            return 0;
        }
    }
}
