package mapedit;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

public class MySpinner extends JSpinner {
	
	public MySpinner(int max) {
		super(new SpinnerNumberModel(0, 0, max, 1));
		PlainDocument doc = new NumberDocument();
		doc.setDocumentFilter(new NumberFilter(max));
		((NumberEditor) this.getEditor()).getTextField().setDocument(doc);
		((NumberEditor) this.getEditor()).getTextField().setText("0");
	}
	
	public void setMax(int max) {
		((PlainDocument) ((NumberEditor) this.getEditor()).getTextField().getDocument()).setDocumentFilter(new NumberFilter(max));
	}
	
	private class NumberDocument extends PlainDocument {
		
		@Override
		public void setDocumentFilter(DocumentFilter filter) {
			if (filter instanceof NumberFilter) {
				super.setDocumentFilter(filter);
			}
		}
	}
	
	public void setValue(int value) {
		this.setValue(new Integer(value));
	}
	
	@Override
	public Integer getValue() {
		return (Integer) super.getValue();
	}
	
}
