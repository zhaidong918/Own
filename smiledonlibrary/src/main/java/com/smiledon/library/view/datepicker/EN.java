package com.smiledon.library.view.datepicker;

/**
 * 英文的默认实现类
 * 如果你想实现更多的语言请参考Language{@link Language}
 * The implementation class of english.
 * You can refer to Language{@link Language} if you want to define more language.
 *
 * @author XZ
 */
public class EN extends Language {
    @Override
    public String[] monthTitles() {
        return new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    }

    @Override
    public String ensureTitle() {
        return "Ok";
    }
}
