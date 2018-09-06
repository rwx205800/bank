package com.yj.gyl.bank.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 
 */
public class TMerchant implements Serializable {
    private Long id;

    /**
     * 商户账户
     */
    private String merchantAccount;

    /**
     * 商户公钥
     */
    private String merchantPublicKey;

    /**
     * 商户私钥
     */
    private String merchantPrivateKey;

    /**
     * 支付通道公钥
     */
    private String publicKey;

    /**
     * 交易通道名称
     */
    private String channel;

    /**
     * 备注
     */
    private String description;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMerchantAccount() {
        return merchantAccount;
    }

    public void setMerchantAccount(String merchantAccount) {
        this.merchantAccount = merchantAccount;
    }

    public String getMerchantPublicKey() {
        return merchantPublicKey;
    }

    public void setMerchantPublicKey(String merchantPublicKey) {
        this.merchantPublicKey = merchantPublicKey;
    }

    public String getMerchantPrivateKey() {
        return merchantPrivateKey;
    }

    public void setMerchantPrivateKey(String merchantPrivateKey) {
        this.merchantPrivateKey = merchantPrivateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        TMerchant other = (TMerchant) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getMerchantAccount() == null ? other.getMerchantAccount() == null : this.getMerchantAccount().equals(other.getMerchantAccount()))
            && (this.getMerchantPublicKey() == null ? other.getMerchantPublicKey() == null : this.getMerchantPublicKey().equals(other.getMerchantPublicKey()))
            && (this.getMerchantPrivateKey() == null ? other.getMerchantPrivateKey() == null : this.getMerchantPrivateKey().equals(other.getMerchantPrivateKey()))
            && (this.getPublicKey() == null ? other.getPublicKey() == null : this.getPublicKey().equals(other.getPublicKey()))
            && (this.getChannel() == null ? other.getChannel() == null : this.getChannel().equals(other.getChannel()))
            && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getMerchantAccount() == null) ? 0 : getMerchantAccount().hashCode());
        result = prime * result + ((getMerchantPublicKey() == null) ? 0 : getMerchantPublicKey().hashCode());
        result = prime * result + ((getMerchantPrivateKey() == null) ? 0 : getMerchantPrivateKey().hashCode());
        result = prime * result + ((getPublicKey() == null) ? 0 : getPublicKey().hashCode());
        result = prime * result + ((getChannel() == null) ? 0 : getChannel().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", merchantAccount=").append(merchantAccount);
        sb.append(", merchantPublicKey=").append(merchantPublicKey);
        sb.append(", merchantPrivateKey=").append(merchantPrivateKey);
        sb.append(", publicKey=").append(publicKey);
        sb.append(", channel=").append(channel);
        sb.append(", description=").append(description);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}