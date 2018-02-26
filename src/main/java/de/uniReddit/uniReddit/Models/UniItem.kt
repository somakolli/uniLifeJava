package de.uniReddit.uniReddit.Models

import javax.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorValue("I")
@DiscriminatorColumn(name = "UNI_ITEM_TYPE")
@Table(name = "UNI_ITEMS")
abstract class UniItem(@ManyToOne var university: University) : Node(){
    @Transient
    fun getUniversityId() : Long {
        return university.id
    }
}
