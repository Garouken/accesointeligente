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
package org.accesointeligente.server.robots;

import org.accesointeligente.model.Institution;
import org.accesointeligente.server.HibernateUtil;
import org.accesointeligente.server.RobotContext;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;

import java.util.List;

public class RobotChecker {
	private static final Logger logger = Logger.getLogger(RobotChecker.class);

	public void checkRobots() {
		Session hibernate = null;

		try {
			hibernate = HibernateUtil.getSession();
			hibernate.beginTransaction();
			Criteria criteria = hibernate.createCriteria(Institution.class);
			List<Institution> institutions = criteria.list();
			hibernate.getTransaction().commit();

			for (Institution institution : institutions) {
				Robot robot = RobotContext.getRobot(institution.getInstitutionClass());

				if (robot != null) {
					try {
						robot.login();
						institution.setCanLogin(true);
					} catch (Exception ex) {
						institution.setCanLogin(false);
					}

					if (institution.getCanLogin()) {
						try {
							institution.setCanMakeRequest(robot.checkInstitutionId());
						} catch (Exception ex) {
							institution.setCanMakeRequest(false);
						}
					} else {
						institution.setCanMakeRequest(false);
					}
				} else {
					institution.setCanLogin(false);
					institution.setCanMakeRequest(false);
				}

				institution.setEnabled(institution.getCanLogin() && institution.getCanMakeRequest());
				hibernate = HibernateUtil.getSession();
				hibernate.beginTransaction();
				hibernate.update(institution);
				hibernate.getTransaction().commit();
			}
		} catch (Exception ex) {
			if (hibernate != null && hibernate.isOpen() && hibernate.getTransaction().isActive()) {
				hibernate.getTransaction().rollback();
				logger.error("Failure", ex);
			}
		}
	}
}
