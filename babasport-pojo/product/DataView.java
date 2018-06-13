package cn.itcast.core.bean.product;

import java.io.Serializable;

public class DataView implements Serializable {
    /**
     * 序号，自动累加
     */
    private Long id;

    /**
     * 分类名称
     */
    private String optionName;

    /**
     * 分类名称
     */
    private String praentName;

    /**
     * 0不可用，1可用
     */
    private Boolean isshow;

    /**
     * 上级分类号
     */
    private Long optionLevel;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName == null ? null : optionName.trim();
    }

    public String getPraentName() {
        return praentName;
    }

    public void setPraentName(String praentName) {
        this.praentName = praentName == null ? null : praentName.trim();
    }

    public Boolean getIsshow() {
        return isshow;
    }

    public void setIsshow(Boolean isshow) {
        this.isshow = isshow;
    }

    public Long getOptionLevel() {
        return optionLevel;
    }

    public void setOptionLevel(Long optionLevel) {
        this.optionLevel = optionLevel;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", optionName=").append(optionName);
        sb.append(", praentName=").append(praentName);
        sb.append(", isshow=").append(isshow);
        sb.append(", optionLevel=").append(optionLevel);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}