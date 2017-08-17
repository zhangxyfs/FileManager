package com.z7dream.android_filemanager_lib.tool.collator;

import com.z7dream.android_filemanager_lib.model.MagicFileInfo;

import java.text.Collator;
import java.util.Locale;

/**
 * Created by Z7Dream on 2017/3/31 14:51.
 * Email:zhangxyfs@126.com
 */

public interface OrderingConstants {
    Collator collator = Collator.getInstance(Locale.CHINA);

    /**
     * Model为要排序的对象model，如Person，等等类型的自定义model。
     */
    Ordering<MagicFileInfo> Model_NAME_ORDERING = new Ordering<MagicFileInfo>() {
        @Override
        public int compare(MagicFileInfo left, MagicFileInfo right) {
            if (left == null || left.fileName == null) {
                return -1;
            }
            if (right == null || right.fileName == null) {
                return 1;
            }
            return collator.compare(left.fileName, right.fileName);
        }
    };
}
