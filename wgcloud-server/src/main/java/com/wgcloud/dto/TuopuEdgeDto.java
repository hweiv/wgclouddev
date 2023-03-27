package com.wgcloud.dto;

import cn.hutool.json.JSONObject;
import java.io.Serializable;

public class TuopuEdgeDto implements Serializable {
   private String source;
   private String target;
   private JSONObject style;
   private String type;

   public String getSource() {
      return this.source;
   }

   public void setSource(String source) {
      this.source = source;
   }

   public String getTarget() {
      return this.target;
   }

   public void setTarget(String target) {
      this.target = target;
   }

   public JSONObject getStyle() {
      return this.style;
   }

   public void setStyle(JSONObject style) {
      this.style = style;
   }

   public String getType() {
      return this.type;
   }

   public void setType(String type) {
      this.type = type;
   }
}
