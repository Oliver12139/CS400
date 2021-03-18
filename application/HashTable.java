package application;
/**
 * HashTable.java created by Vivian Ye on MacBook Pro in ATeamProject Apr 23, 2020
 *
 * Author:	Xunwei Ye (xye53@wisc.edu)
 * Date:	Apr 23. 2020
 * 
 * Course:	CS400
 * Semester:	Spring 2020
 * Lecture:	001
 * 
 * IDE:		Eclipse IDE for Java Developers
 * Version: 	2019-06 (4.12.0)
 * Build id:	20190614-1200
 * 
 * Device:	binhaochen's Macbook Pro
 * OS:		macOS 
 * Version: 	10.15.3
 * OS Build:	N/A
 *
 * List Collaborators: NONE
 *
 * Other Credits: NONE
 *
 * Known Bugs: NONE
 */
import java.util.*;

/**
 * 
 * HashTable - self-implementation of hash table
 * 
 * @author Xunwei Ye
 *
 * @param <K> the key type of node in the hash table
 * @param <V> the value type of node in the hash table
 */
public class HashTable<K extends Comparable<K>, V> implements HashTableADT<K, V> {

	private int capacity; // the table size of hash table
	private int numberOfKeys; // the sum of keys stored in the hash table
	private double loadFactorThreshold; // number of items / current table size
	private Object[] hashTable; // the hash table used to store items

	/**
	 * 
	 * Node - node storing key and value pairs
	 * 
	 * @author Xunwei Ye
	 *
	 */
	private class Node {
		private K key; // key of the Node
		private V value; // value of the Node
		private Node nextNode; // the linked next Node
	}

	/**
	 * HashTable's non-argument constructor, initialize fields of hash table
	 */
	public HashTable() {
		this.capacity = 499973;
		this.numberOfKeys = 0;
		this.loadFactorThreshold = 0.75;
		hashTable = new Object[this.capacity];
	}

	/**
	 * HashTable's constructor, initialize fields of hash table
	 * 
	 * @param initialCapacity     the initial capacity of hash table
	 * @param loadFactorThreshold the assigned load factor threshold of hash table
	 */
	public HashTable(int initialCapacity, double loadFactorThreshold) {
		this.capacity = initialCapacity;
		this.numberOfKeys = 0;
		this.loadFactorThreshold = loadFactorThreshold;
		hashTable = new Object[this.capacity];
	}

	/**
	 * Add the key, value pair to the hash table and increase the number of keys. If
	 * the key is already in the hash table, replace value with new value
	 * 
	 * @param key   the key of the item to be added
	 * @param value the value of the item to be added
	 * @throws IllegalNullKeyException if key is null
	 */
	@Override
	public void insert(K key, V value) throws IllegalNullKeyException {
		if (key == null) {
			throw new IllegalNullKeyException();
		}

		Node newNode = new Node();
		newNode.key = key;
		newNode.value = value;
		newNode.nextNode = null;
		int hashIndex = Math.abs(key.hashCode() % this.capacity);

		if (this.hashTable[hashIndex] == null) {
			this.hashTable[hashIndex] = newNode;
			this.numberOfKeys++;
		} else {
			Node currentNode = (Node) this.hashTable[hashIndex];
			boolean flag = false;
			while (currentNode.nextNode != null) {
				if (currentNode.key.equals(key) == true) {
					currentNode.value = value;
					flag = true;
				}
				currentNode = currentNode.nextNode;
			}
			if (currentNode.key.equals(key) == true) {
				currentNode.value = value;
				flag = true;
			}
			if (flag == false) {
				currentNode.nextNode = newNode;
				this.numberOfKeys++;
			}

		}

		// resize and rehash the hash table when reaching the load factor threshold
		if (this.getLoadFactor() >= this.loadFactorThreshold) {
			int newCapacity = this.capacity * 2 + 1;
			HashTable<K, V> newTable = new HashTable<>(newCapacity, this.loadFactorThreshold);
			for (int i = 0; i < this.capacity; i++) {
				Node currentNode = (Node) this.hashTable[i];
				while (currentNode != null) {
					newTable.insert(currentNode.key, currentNode.value);
					currentNode = currentNode.nextNode;
				}
			}
			this.hashTable = newTable.hashTable;
			this.capacity = newCapacity;
		}

	}

	/**
	 * If key is found,remove the key,value pair from the hash table, and decrease
	 * number of keys.
	 * 
	 * @param key the key of the node to be removed
	 * @throws IllegalNullKeyException if key is null
	 * @return true if key is found, and false if it is not found
	 */
	@Override
	public boolean remove(K key) throws IllegalNullKeyException {
		if (key == null) {
			throw new IllegalNullKeyException();
		}

		int hashIndex = Math.abs(key.hashCode() % this.capacity);

		if (this.hashTable[hashIndex] == null) {
			return false;
		} else {
			Node currentNode = (Node) this.hashTable[hashIndex];
			if (currentNode.key.equals(key) == true) {
				this.hashTable[hashIndex] = currentNode.nextNode;
				this.numberOfKeys--;
				return true;
			} else {
				while (currentNode.nextNode != null && currentNode.nextNode.key.equals(key) != true) {
					currentNode = currentNode.nextNode;
				}

				if (currentNode.nextNode == null) {
					return false;
				} else {
					currentNode.nextNode = currentNode.nextNode.nextNode;
					this.numberOfKeys--;
					return true;
				}
			}
		}

	}

	/**
	 * Returns the value associated with the specified key
	 * 
	 * @param key the node's key
	 * 
	 * @throws IllegalNullKeyException if key is null
	 * @throws KeyNotFoundException    if key is not found
	 * 
	 * @return value associated with the specified key
	 */
	@Override
	public V get(K key) throws IllegalNullKeyException, KeyNotFoundException {
		if (key == null) {
			throw new IllegalNullKeyException();
		}

		int hashIndex = Math.abs(key.hashCode() % this.capacity);
		if (this.hashTable[hashIndex] == null) {
			throw new KeyNotFoundException();
		} else {
			Node currentNode = (Node) this.hashTable[hashIndex];
			while (currentNode != null && currentNode.key.equals(key) != true) {
				currentNode = currentNode.nextNode;
			}

			if (currentNode == null) {
				throw new KeyNotFoundException();
			} else {
				return currentNode.value;
			}
		}

	}

	/**
	 * @return the number of key,value pairs in the hash table
	 */
	@Override
	public int numKeys() {
		return this.numberOfKeys;
	}

	/**
	 * @return the load factor threshold that was passed into the constructor
	 */
	@Override
	public double getLoadFactorThreshold() {
		return this.loadFactorThreshold;
	}

	/**
	 * @return the current load factor for this hash table
	 */
	@Override
	public double getLoadFactor() {
		return (double) this.numberOfKeys / (double) this.capacity;
	}

	/**
	 * @return the current Capacity (table size) of the hash table array.
	 */
	@Override
	public int getCapacity() {
		return this.capacity;
	}

	/**
	 * @return the collision resolution scheme used for this hash table
	 */
	@Override
	public int getCollisionResolution() {
		return 5;
	}

}
