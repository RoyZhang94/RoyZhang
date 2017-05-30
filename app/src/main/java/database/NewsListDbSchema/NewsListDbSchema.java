package database.NewsListDbSchema;

import net.tsz.afinal.FinalActivity;

/**
 * Created by Roy on 2017/4/12.
 */

public class NewsListDbSchema extends FinalActivity {
    public static final class NewsListTable{
        public static final String NAME = "NewsList";

        public static final class Cols{
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String IMAGE = "image";

        }
    }
}
