package de.uniReddit.uniReddit.Models

import javax.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "OBJECT_TYPE")
@Table(name = "NODES")
abstract class Node{
    @Id @GeneratedValue(strategy = GenerationType.AUTO) var id: Long = 0
}
