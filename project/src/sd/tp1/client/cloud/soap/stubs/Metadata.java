
package sd.tp1.client.cloud.soap.stubs;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for metadata complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="metadata">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="deleted" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="logicClock" type="{http://soap.server.tp1.sd/}lamportLogicClock" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "metadata", propOrder = {
    "deleted",
    "logicClock"
})
public class Metadata {

    protected boolean deleted;
    protected LamportLogicClock logicClock;

    /**
     * Gets the value of the deleted property.
     * 
     */
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * Sets the value of the deleted property.
     * 
     */
    public void setDeleted(boolean value) {
        this.deleted = value;
    }

    /**
     * Gets the value of the logicClock property.
     * 
     * @return
     *     possible object is
     *     {@link LamportLogicClock }
     *     
     */
    public LamportLogicClock getLogicClock() {
        return logicClock;
    }

    /**
     * Sets the value of the logicClock property.
     * 
     * @param value
     *     allowed object is
     *     {@link LamportLogicClock }
     *     
     */
    public void setLogicClock(LamportLogicClock value) {
        this.logicClock = value;
    }

}
