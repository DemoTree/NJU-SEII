package com.example.cinema.vo;

import com.example.cinema.po.Hall;

/**
 * @author zzy
 * @Date 2019-05-31
 */
public class HallForm {
    /**
     * 影厅编号
     */
    private Integer id;

    /**
     * 影厅名称
     */
    private String name;

    /**
     * 影厅行排数
     */
    private Integer row;

    /**
     * 影厅列数
     */
    private Integer column;

    /**
     * 影厅种类
     */
//	private String type;
    private Integer type;

    public HallForm(){

    }

//	public HallForm(Hall hall){
//		this.id = hall.getId();
//		this.name = hall.getName();
//		this.row = hall.getRow();
//		this.column = hall.getColumn();
//		Integer typeInteger=0;
//		switch(hall.getType()){
//			case "2D厅":
//				typeInteger = 1;
//				break;
//			case "3D厅":
//				typeInteger = 2;
//				break;
//			case "IMAX厅":
//				typeInteger = 3;
//				break;
//			default:
//				typeInteger = 0;
//		}
//		this.type = hall.getType();
//	}

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

//	public String getType() {
//		return type;
//	}
//
//	public void setType(String type) {
//		this.type = type;
//	}

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
