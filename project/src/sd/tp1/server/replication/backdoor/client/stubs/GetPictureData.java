
package sd.tp1.server.replication.backdoor.client.stubs;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getPictureData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getPictureData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="arg0" type="{http://backdoor.replication.server.tp1.sd/}sharedAlbum" minOccurs="0"/>
 *         &lt;element name="arg1" type="{http://backdoor.replication.server.tp1.sd/}sharedPicture" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getPictureData", propOrder = {
    "arg0",
    "arg1"
})
public class GetPictureData {

    protected SharedAlbum arg0;
    protected SharedPicture arg1;

    /**
     * Gets the value of the arg0 property.
     * 
     * @return
     *     possible object is
     *     {@link SharedAlbum }
     *     
     */
    public SharedAlbum getArg0() {
        return arg0;
    }

    /**
     * Sets the value of the arg0 property.
     * 
     * @param value
     *     allowed object is
     *     {@link SharedAlbum }
     *     
     */
    public void setArg0(SharedAlbum value) {
        this.arg0 = value;
    }

    /**
     * Gets the value of the arg1 property.
     * 
     * @return
     *     possible object is
     *     {@link SharedPicture }
     *     
     */
    public SharedPicture getArg1() {
        return arg1;
    }

    /**
     * Sets the value of the arg1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link SharedPicture }
     *     
     */
    public void setArg1(SharedPicture value) {
        this.arg1 = value;
    }

}
