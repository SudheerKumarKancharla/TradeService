package com.client.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.GenericGenerator;

import com.broker.external.BrokerTradeSide;
import com.client.service.IdGenerator;

/**
 * Entity class to represent Trade table in Database
 */
@Entity
@Table(name = "trade")
public class Trade implements Serializable {

    private static final long serialVersionUID = 1L;

    private String Id;
    private long quantity;
    private String symbol;
    private BrokerTradeSide side;
    private BigDecimal price;
    private String status;
    private String reason = null;
    private Date timeStamp;

    public Trade() {
    }

    public Trade(String id, long quantity, String symbol, BrokerTradeSide side, BigDecimal price, String status,
            String reason, Date timeStamp) {
        Id = id;
        this.quantity = quantity;
        this.symbol = symbol;
        this.side = side;
        this.price = price;
        this.status = status;
        this.reason = reason;
        this.timeStamp = timeStamp;
    }

    @Id
    @GeneratedValue(generator = IdGenerator.generatorName)
    @GenericGenerator(name = IdGenerator.generatorName, strategy = "com.client.service.IdGenerator")
    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    @Min(value = 1, message = "must be greater than 0 and less than or equal to 1M")
    @Max(value = 1000000, message = "must be greater than 0 and less than or equal to 1M")
    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    @NotNull
    @Pattern(regexp = "EUR/USD|USD/JPY", message = "valid values: USD/JPY, EUR/USD")
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BrokerTradeSide getSide() {
        return side;
    }

    public void setSide(BrokerTradeSide side) {
        this.side = side;
    }

    @NotNull
    @DecimalMin(value = "1", message = "must be greater than 0")
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "Trade [Id=" + Id + ", price=" + price + ", quantity=" + quantity + ", reason=" + reason + ", side="
                + side + ", status=" + status + ", symbol=" + symbol + ", timeStamp=" + timeStamp + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((Id == null) ? 0 : Id.hashCode());
        result = prime * result + ((price == null) ? 0 : price.hashCode());
        result = prime * result + (int) (quantity ^ (quantity >>> 32));
        result = prime * result + ((reason == null) ? 0 : reason.hashCode());
        result = prime * result + ((side == null) ? 0 : side.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
        result = prime * result + ((timeStamp == null) ? 0 : timeStamp.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Trade other = (Trade) obj;
        if (Id == null) {
            if (other.Id != null)
                return false;
        } else if (!Id.equals(other.Id))
            return false;
        if (price == null) {
            if (other.price != null)
                return false;
        } else if (!price.equals(other.price))
            return false;
        if (quantity != other.quantity)
            return false;
        if (reason == null) {
            if (other.reason != null)
                return false;
        } else if (!reason.equals(other.reason))
            return false;
        if (side != other.side)
            return false;
        if (status == null) {
            if (other.status != null)
                return false;
        } else if (!status.equals(other.status))
            return false;
        if (symbol == null) {
            if (other.symbol != null)
                return false;
        } else if (!symbol.equals(other.symbol))
            return false;
        if (timeStamp == null) {
            if (other.timeStamp != null)
                return false;
        } else if (!timeStamp.equals(other.timeStamp))
            return false;
        return true;
    }

    

}
