
package sd.tp1.server.replication.backdoor.client.stubs;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *         &lt;element name="album" type="{http://backdoor.replication.server.tp1.sd/}sharedAlbum" minOccurs="0"/>
 *         &lt;element name="deleted" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="local" type="{http://backdoor.replication.server.tp1.sd/}serverMetadata" minOccurs="0"/>
 *         &lt;element name="logicClock" type="{http://backdoor.replication.server.tp1.sd/}lamportLogicClock" minOccurs="0"/>
 *         &lt;element name="picture" type="{http://backdoor.replication.server.tp1.sd/}sharedPicture" minOccurs="0"/>
 *         &lt;element name="sourceSet" type="{http://backdoor.replication.server.tp1.sd/}serverMetadata" maxOccurs="unbounded" minOccurs="0"/>
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
    "album",
    "deleted",
    "local",
    "logicClock",
    "picture",
    "sourceSet"
})
public class Metadata {

    protected SharedAlbum album;
    protected boolean deleted;
    protected ServerMetadata local;
    protected LamportLogicClock logicClock;
    protected SharedPicture picture;
    @XmlElement(nillable = true)
    protected List<ServerMetadata> sourceSet;

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
     * Gets the value of the local property.
     * 
     * @return
     *     possible object is
     *     {@link ServerMetadata }
     *     
     */
    public ServerMetadata getLocal() {
        return local;
    }

    /**
     * Sets the value of the local property.
     * 
     * @param value
     *     allowed object is
     *     {@link ServerMetadata }
     *     
     */
    public void setLocal(ServerMetadata value) {
        this.local = value;
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

    /**
     * Gets the value of the sourceSet property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sourceSet property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSourceSet().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ServerMetadata }
     * 
     * 
     */
    public List<ServerMetadata> getSourceSet() {
        if (sourceSet == null) {
            sourceSet = new ArrayList<ServerMetadata>();
        }
        return this.sourceSet;
    }

}
