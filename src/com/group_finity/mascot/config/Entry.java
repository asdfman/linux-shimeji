package com.group_finity.mascot.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Entry {

	private Element element;
	
	private Map<String, String> attributes;
	
	private List<Entry> children;
	
	private Map<String, List<Entry> > selected = new HashMap<String, List<Entry>>();
	
	public Entry(final Element element){
		this.element = element;
	}
	
	public String getName() {
		return this.element.getTagName();
	}
	
	public Map<String, String> getAttributes() {
		if ( this.attributes!=null) {
			return this.attributes;
		}
		
		this.attributes = new LinkedHashMap<String, String>();
		final NamedNodeMap attrs = this.element.getAttributes();
		for(int i = 0; i<attrs.getLength(); ++i ) {
			final Attr attr = (Attr)attrs.item(i);
			this.attributes.put(attr.getName(), attr.getValue());
		}
		
		return this.attributes;
	}
	
	public String getAttribute(final String attributeName){
		final Attr attribute = this.element.getAttributeNode(attributeName);
		if ( attribute==null ) {
			return null;
		}
		return attribute.getValue();
	}
	
	public List<Entry> selectChildren(final String tagName) {
		
		List<Entry> children = this.selected.get(tagName);
		if ( children!=null ) {
			return children;
		}
		children = new ArrayList<Entry>();
		for( final Entry child : getChildren() ) {
			if ( child.getName().equals(tagName)) {
				children.add(child);
			}
		}
		
		this.selected.put(tagName, children);
		
		return children;
	}

	public List<Entry> getChildren() {
		
		if ( this.children!=null) {
			return this.children;
		}
		
		this.children = new ArrayList<Entry>();
		final NodeList childNodes = this.element.getChildNodes();
		for( int i = 0; i<childNodes.getLength(); ++i) {
			final Node childNode = childNodes.item(i);
			if ( childNode instanceof Element ) {
				this.children.add(new Entry((Element)childNode));
			}
		}
		
		return this.children;
	}
}
