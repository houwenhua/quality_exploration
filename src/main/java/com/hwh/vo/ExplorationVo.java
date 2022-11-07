package com.hwh.vo;

public class ExplorationVo {
    private String db_name;
    private String table_name;
    private String table_comment;
    private String column_name;
    private String column_comment;
    private String primary_key;
    private String table_num;
    private String distinct_num;
    private String no_null_num;
    private String valuable_rate;
    private String repeat_rate;

    public ExplorationVo() {
    }

    public ExplorationVo(String db_name, String table_name, String table_comment, String column_name, String column_comment, String primary_key, String table_num, String distinct_num, String no_null_num, String valuable_rate, String repeat_rate) {
        this.db_name = db_name;
        this.table_name = table_name;
        this.table_comment = table_comment;
        this.column_name = column_name;
        this.column_comment = column_comment;
        this.primary_key = primary_key;
        this.table_num = table_num;
        this.distinct_num = distinct_num;
        this.no_null_num = no_null_num;
        this.valuable_rate = valuable_rate;
        this.repeat_rate = repeat_rate;
    }

    public String getDb_name() {
        return db_name;
    }

    public String getTable_name() {
        return table_name;
    }

    public String getTable_comment() {
        return table_comment;
    }

    public String getColumn_name() {
        return column_name;
    }

    public String getColumn_comment() {
        return column_comment;
    }

    public String getTable_num() {
        return table_num;
    }

    public String getDistinct_num() {
        return distinct_num;
    }

    public String getNo_null_num() {
        return no_null_num;
    }

    public String getPrimary_key() {
        return primary_key;
    }

    public String getValuable_rate() {
        return valuable_rate;
    }

    public String getRepeat_rate() {
        return repeat_rate;
    }

    public void setDb_name(String db_name) {
        this.db_name = db_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public void setTable_comment(String table_comment) {
        this.table_comment = table_comment;
    }

    public void setColumn_name(String column_name) {
        this.column_name = column_name;
    }

    public void setColumn_comment(String column_comment) {
        this.column_comment = column_comment;
    }

    public void setTable_num(String table_num) {
        this.table_num = table_num;
    }

    public void setDistinct_num(String distinct_num) {
        this.distinct_num = distinct_num;
    }

    public void setNo_null_num(String no_null_num) {
        this.no_null_num = no_null_num;
    }

    public void setPrimary_key(String primary_key) {
        this.primary_key = primary_key;
    }

    public void setValuable_rate(String valuable_rate) {
        this.valuable_rate = valuable_rate;
    }

    public void setRepeat_rate(String repeat_rate) {
        this.repeat_rate = repeat_rate;
    }

    @Override
    public String toString() {
        return "ExplorationVo{" +
                "db_name='" + db_name + '\'' +
                ", table_name='" + table_name + '\'' +
                ", table_comment='" + table_comment + '\'' +
                ", column_name='" + column_name + '\'' +
                ", column_comment='" + column_comment + '\'' +
                ", primary_key='" + primary_key + '\'' +
                ", table_num='" + table_num + '\'' +
                ", distinct_num='" + distinct_num + '\'' +
                ", no_null_num='" + no_null_num + '\'' +
                ", valuable_rate='" + valuable_rate + '\'' +
                ", repeat_rate='" + repeat_rate + '\'' +
                '}';
    }
}
