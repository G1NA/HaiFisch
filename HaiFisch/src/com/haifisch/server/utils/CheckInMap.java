package com.haifisch.server.utils;

import java.util.*;

/**
 * A map for the checkins extending the TreeMap which implements the SortedMap interface.
 * This map 
 */

public class CheckInMap<K,V extends Comparable<V>> extends TreeMap<K,V>{

	private static final long serialVersionUID = 4846756247593569653L;
	
	public CheckInMap(){
		// set a comparator based on the values: the number of checkins
		super(new Comparator<K>() {
			@SuppressWarnings("unchecked")
			public int compare(Object o1 , Object o2)
            {
                Map.Entry<K,V> e1 = (Map.Entry<K,V>) o1 ;
                Map.Entry<K,V> e2 = (Map.Entry<K,V>) o2 ;
                return e1.getValue().compareTo(e2.getValue());
            }
		});
	}
	
	
}
