package org.accesointeligente.client.widgets;

import org.accesointeligente.client.AnchorCell;
import org.accesointeligente.client.AnchorCellParams;
import org.accesointeligente.model.Attachment;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.ListDataProvider;

import java.util.Date;

public class ResponseWidget extends Composite {
	private static ResponseWidgetUiBinder uiBinder = GWT.create(ResponseWidgetUiBinder.class);
	interface ResponseWidgetUiBinder extends UiBinder<Widget, ResponseWidget> {}

	@UiField Label responseDate;
	@UiField Label responseInfo;
	@UiField HTMLPanel responseAttachmentsPanel;
	@UiField CellTable<Attachment> attachmentsTable;

	public ResponseWidget() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setInfo(String info) {
		responseInfo.setText(info);
	}

	public void setDate(Date date) {
		responseDate.setText(DateTimeFormat.getFormat("dd/MM/yyyy HH:mm").format(date));
	}

	public void initTableColumns() {
		// Name
		Column<Attachment, String> nameColumn = new Column<Attachment, String>(new TextCell()) {
			@Override
			public String getValue(Attachment attachment) {
				return attachment.getName();
			}
		};
		attachmentsTable.addColumn(nameColumn, "Nombre");

		// Type
		Column<Attachment, String> typeColumn = new Column<Attachment, String>(new TextCell()) {
			@Override
			public String getValue(Attachment attachment) {
				return attachment.getType().getName() + " " + attachment.getType().getExtension();
			}
		};
		attachmentsTable.addColumn(typeColumn, "Tipo");

		// Download
		Column<Attachment, AnchorCellParams> statusColumn = new Column<Attachment, AnchorCellParams>(new AnchorCell()) {
			@Override
			public AnchorCellParams getValue(Attachment attachment) {
				AnchorCellParams params = new AnchorCellParams();
				params.setUrl(attachment.getUrl());
				params.setStyleNames("");
				params.setValue(attachment.getName() + attachment.getType().getExtension());
				return params;
			}
		};
		attachmentsTable.addColumn(statusColumn, "Descarga");
	}

	public void setResponseAttachments(ListDataProvider<Attachment> data) {
		data.addDataDisplay(attachmentsTable);
	}
}
