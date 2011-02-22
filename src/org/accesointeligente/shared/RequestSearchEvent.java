/**
 * Acceso Inteligente
 *
 * Copyright (C) 2010-2011 Fundación Ciudadano Inteligente
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.accesointeligente.shared;

import com.google.gwt.event.shared.GwtEvent;

public class RequestSearchEvent extends GwtEvent<RequestSearchEventHandler> {

	private RequestSearchParams params;
	public static final Type<RequestSearchEventHandler> TYPE = new Type<RequestSearchEventHandler>();

	public RequestSearchEvent(RequestSearchParams params) {
		this.params = params;
	}

	public RequestSearchParams getParams() {
		return params;
	}

	public void setParams(RequestSearchParams params) {
		this.params = params;
	}

	@Override
	public Type<RequestSearchEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(RequestSearchEventHandler handler) {
		handler.onSearch(this);
	}
}
