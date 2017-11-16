package com.yidiankeyan.science.information.entity;

import com.yidiankeyan.science.my.entity.EditorAlbum;

import java.util.ArrayList;

/**
 * //  ◥█▄▃▁   ▁▂▂▃▃▂▂▂▂▂▂▂▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▅
 * //.......◥█☆█▅▄▃▁▁▁▁▁▃▄▅▅▅▅▅▅▅▅▅▅▅▅▅▅▄▁█▅●    ...Created by zn on 2017/4/12 0012.
 * //〓▇█████ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓███▅▄▃███●
 * // 〓〓.๑۩۞۩๑.█████████████████████◤
 * //   ◥█████████◤ ◥        ◢◤
 * //     ◥██████◤        ◥  ◢◤             音频
 * //       ████◤██████◤                        -所有音频
 * //       █▓▓▓▓██◤
 * //      █▓▓▓██◆
 * //     █▓▓▓██◆
 * //    █▓▓▓██◆
 * //   █▓▓▓██◆
 * //  █▓▓▓██◆
 * // █▓▓ ██◆
 * //█▓▓ ██◆
 */
public class AllAudioListBean {

    private int subjectid;

    public int getSubjectid() {
        return subjectid;
    }

    public void setSubjectid(int subjectid) {
        this.subjectid = subjectid;
    }

    private ArrayList<EditorAlbum> editorAlba;

    public AllAudioListBean(ArrayList<EditorAlbum> editorAlba){
        this.editorAlba=editorAlba;
    }

    public ArrayList<EditorAlbum> getEditorAlba() {
        return editorAlba;
    }

    public void setEditorAlba(ArrayList<EditorAlbum> editorAlba) {
        this.editorAlba = editorAlba;
    }
}
