package com.baker.tts.mix.utils;

import com.excel.utils.annotation.CellStyle;
import com.excel.utils.annotation.ExcelReadCell;
import com.excel.utils.annotation.ExcelTable;
import com.excel.utils.annotation.ExcelWriteCell;

import org.apache.poi.ss.usermodel.FontUnderline;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

@ExcelTable(sheetName = "tts")
public class DataBean {
    @ExcelReadCell(name = "文本")
    @ExcelWriteCell(writeIndex = 0, writeName = "文本")
    @CellStyle(horizontalAlign = HorizontalAlignment.CENTER, verticalAlign = VerticalAlignment.CENTER, underline = FontUnderline.NONE)
    public String text;

    @ExcelReadCell(name = "音频时长")
    @ExcelWriteCell(writeIndex = 1, writeName = "音频时长")
    @CellStyle(horizontalAlign = HorizontalAlignment.CENTER, verticalAlign = VerticalAlignment.CENTER, underline = FontUnderline.NONE)
    public String audioLength;

    @ExcelReadCell(name = "合成时长")
    @ExcelWriteCell(writeIndex = 2, writeName = "合成时长")
    @CellStyle(horizontalAlign = HorizontalAlignment.CENTER, verticalAlign = VerticalAlignment.CENTER, underline = FontUnderline.NONE)
    public long synthesisTime;

    @ExcelReadCell(name = "实时率")
    @ExcelWriteCell(writeIndex = 3, writeName = "实时率")
    @CellStyle(horizontalAlign = HorizontalAlignment.CENTER, verticalAlign = VerticalAlignment.CENTER, underline = FontUnderline.NONE)
    public String rate;

    @ExcelReadCell(name = "首包时间")
    @ExcelWriteCell(writeIndex = 4, writeName = "首包时间")
    @CellStyle(horizontalAlign = HorizontalAlignment.CENTER, verticalAlign = VerticalAlignment.CENTER, underline = FontUnderline.NONE)
    public long firstTime;

    @ExcelReadCell(name = "建立ws耗时")
    @ExcelWriteCell(writeIndex = 5, writeName = "建立ws耗时")
    @CellStyle(horizontalAlign = HorizontalAlignment.CENTER, verticalAlign = VerticalAlignment.CENTER, underline = FontUnderline.NONE)
    public long connectTime;

    public DataBean(String text, String audioLength, long synthesisTime, String rate, long firstTime) {
        this.text = text;
        this.audioLength = audioLength;
        this.synthesisTime = synthesisTime;
        this.rate = rate;
        this.firstTime = firstTime;
    }

    public DataBean(String text, String audioLength, long synthesisTime, String rate, long firstTime, long connectTime) {
        this.text = text;
        this.audioLength = audioLength;
        this.synthesisTime = synthesisTime;
        this.rate = rate;
        this.firstTime = firstTime;
        this.connectTime = connectTime;
    }
}
