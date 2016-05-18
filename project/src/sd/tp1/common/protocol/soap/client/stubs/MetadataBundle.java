
package sd.tp1.common.protocol.soap.client.stubs;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for metadataBundle complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="metadataBundle">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="albumList" type="{http://server.soap.protocol.common.tp1.sd/}sharedAlbum" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="pictureList" type="{http://server.soap.protocol.common.tp1.sd/}sharedAlbumPicture" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "metadataBundle", propOrder = {
    "albumList",
    "pictureList"
})
public class MetadataBundle {

    @XmlElement(nillable = true)
    protected List<SharedAlbum> albumList;
    @XmlElement(nillable = true)
    protected List<SharedAlbumPicture> pictureList;

    /**
     * Gets the value of the albumList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the albumList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAlbumList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SharedAlbum }
     * 
     * 
     */
    public List<SharedAlbum> getAlbumList() {
        if (albumList == null) {
            albumList = new ArrayList<SharedAlbum>();
        }
        return this.albumList;
    }

    /**
     * Gets the value of the pictureList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the pictureList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPictureList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SharedAlbumPicture }
     * 
     * 
     */
    public List<SharedAlbumPicture> getPictureList() {
        if (pictureList == null) {
            pictureList = new ArrayList<SharedAlbumPicture>();
        }
        return this.pictureList;
    }

}
