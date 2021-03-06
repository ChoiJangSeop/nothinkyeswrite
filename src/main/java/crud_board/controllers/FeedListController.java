package crud_board.controllers;

import crud_board.bind.DataBinding;
import crud_board.dao.MySqlFeedDao;
import crud_board.service.FeedService;
import crud_board.vo.Feed;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FeedListController implements Controller, DataBinding {

    //MySqlFeedDao feedDao;
    FeedService feedService;

    public FeedListController setFeedService(FeedService feedService) {
        this.feedService = feedService;
        return this;
    }

    /*
    public FeedListController setFeedDao(MySqlFeedDao feedDao) {
        this.feedDao = feedDao;
        return this;
    }
     */

    @Override
    public Object[] getDataBinders() {
        return new Object[] {
                "text", String.class,
                "record", String.class
        };
    }

    @Override
    public String execute(Map<String, Object> model) throws Exception {
        HttpSession session = (HttpSession) model.get("session");
        List<Feed> feeds = feedService.selectList();
        String text = (String) model.get("text");
        String record = (String) model.get("record");

        if (text != null) {
            List<Feed> searchFeeds = new ArrayList<>();

            // search feed
            for (Feed feed : feeds) {
                if (text.trim().length() == 0) break;

                String title = feed.getTitle().trim();
                String writer = feed.getWriter();

                if (writer.startsWith("익명")) {
                    feed.setWriter("익명");
                }

                if (title.contains(text.trim())) {
                    searchFeeds.add(feed);
                }
            }

            model.put("alert", "'" + text + "' 검색결과, " + searchFeeds.size() + "개의 피드가 검색되었습니다");
            model.put("feeds", searchFeeds);

            // update searchLog
            if (record.equals("true")) {
                List<String> searchLog = (List<String>) session.getAttribute("searchLog");
                searchLog.add(text);

                // searchLog's size maintain 10
                if (searchLog.size() == 11) {
                    searchLog.remove(0);
                }

                session.setAttribute("searchLog", searchLog);
            }
        } else {
            for (Feed feed : feeds) {
                if (feed.getWriter().startsWith("익명")) {
                    feed.setWriter("익명");
                }
            }

            model.put("alert", "");
            model.put("feeds", feeds);
        }

        model.put("mostViewFeeds", feedService.selectMostViewList());
        model.put("mostLikeFeeds", feedService.selectMostLikeList());
        model.put("loginUser", session.getAttribute("loginUser"));
        return "/feed/FeedList.jsp";
    }
}
