package com.onjyb.db;

import android.graphics.Bitmap;
import com.onjyb.Constants;
import java.io.Serializable;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AttachmentMap implements Serializable {
    String attachmentId;
    String attachmentName;
    String attachmentStatus;
    Bitmap bitmap;
    String mapName;
    String refMapId;
    String serverWorkSheetId;
    String syncStatus;
    int type = Constants.ATTACHEMENT_TYPE_BITMAP;

    @JsonProperty("id")
    public String getAttachmentId() {
        return this.attachmentId;
    }

    @JsonProperty("id")
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }

    @JsonProperty("attachment_name")
    public String getAttachmentName() {
        return this.attachmentName;
    }

    @JsonProperty("attachment_name")
    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    @JsonProperty("attachment_status")
    public String getAttachmentStatus() {
        return this.attachmentStatus;
    }

    @JsonProperty("attachment_status")
    public void setAttachmentStatus(String attachmentStatus) {
        this.attachmentStatus = attachmentStatus;
    }

    @JsonProperty("map_name")
    public String getMapName() {
        return this.mapName;
    }

    @JsonProperty("map_name")
    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    @JsonProperty("ref_map_id")
    public String getRefMapId() {
        return this.refMapId;
    }

    @JsonProperty("ref_map_id")
    public void setRefMapId(String refMapId) {
        this.refMapId = refMapId;
    }

    public String getSyncStatus() {
        return this.syncStatus;
    }

    public void setSyncStatus(String syncStatus) {
        this.syncStatus = syncStatus;
    }

    public String getServerWorkSheetId() {
        return this.serverWorkSheetId;
    }

    public void setServerWorkSheetId(String serverWorkSheetId) {
        this.serverWorkSheetId = serverWorkSheetId;
    }

    @JsonIgnore
    public Bitmap getBitmap() {
        return this.bitmap;
    }

    @JsonIgnore
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @JsonIgnore
    public int getType() {
        return this.type;
    }

    @JsonIgnore
    public void setType(int type) {
        this.type = type;
    }
}
