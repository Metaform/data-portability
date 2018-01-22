package org.dataportabilityproject.types.transfer.models.contacts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.dataportabilityproject.types.transfer.models.DataModel;

import java.util.Collection;

/**
 * A collection of contacts as serialized vCards.
 */
public class ContactsModelWrapper extends DataModel {
    private Collection<String> vCards;

    @JsonCreator
    public ContactsModelWrapper(@JsonProperty("vCards") Collection<String> vCards) {
        this.vCards = vCards;
    }

    public Collection<String> getVCards() {
        return vCards;
    }
}
