package com.example.cinema.po;

import com.example.cinema.vo.HallVO;


/**
 * @author fjj
 * @date 2019/4/28 5:09 PM
 */
public class Hall {
    private Integer id;
    private String name;
    private Integer row;
    private Integer column;
    /**
     * 影厅种类
     * 1:2D厅 2:3D厅 3:IMAX厅
     * @author zzy
     * @date 5/31
     */
    private Integer type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getColumn() {
        return column;
    }

    public void setColumn(Integer column) {
        this.column = column;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
