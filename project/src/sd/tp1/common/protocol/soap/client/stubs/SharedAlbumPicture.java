
package sd.tp1.common.protocol.soap.client.stubs;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for sharedAlbumPicture complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="sharedAlbumPicture">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="album" type="{http://server.soap.protocol.common.tp1.sd/}sharedAlbum" minOccurs="0"/>
 *         &lt;element name="picture" type="{http://server.soap.protocol.common.tp1.sd/}sharedPicture" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sharedAlbumPicture", propOrder = {
    "album",
    "picture"
})
public class SharedAlbumPicture {

    protected SharedAlbum album;
    protected SharedPicture picture;

    /**
     * Gets the value of the album property.
     * 
     * @return
     *     possible object is
     *     {@link SharedAlbum }
     *     
     */
    public SharedAlbum getAlbum() {
        return album;
    }

    /**
     * Sets the value of the album property.
     * 
     * @param value
     *     allowed object is
     *     {@link SharedAlbum }
     *     
     */
    public void setAlbum(SharedAlbum value) {
        this.album = value;
    }

    /**
     * Gets the value of the picture property.
     * 
     * @return
     *     possible object is
     *     {@link SharedPicture }
     *     
     */
    public SharedPicture getPicture() {
        return picture;
    }

    /**
     * Sets the value of the picture property.
     * 
     * @param value
     *     allowed object is
     *     {@link SharedPicture }
     *     
     */
    public void setPicture(SharedPicture value) {
        this.picture = value;
    }

}
