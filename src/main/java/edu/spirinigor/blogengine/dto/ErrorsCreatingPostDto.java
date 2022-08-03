package edu.spirinigor.blogengine.dto;

import lombok.Data;

@Data
public class ErrorsCreatingPostDto {
    private String title = "";
    private String text = "";

    public void setTitleField() {
        this.title = "Заголовок не установлен";
    }

    public void setTextField() {
        this.text = "Текст публикации слишком короткий";
    }
}
