package org.holodeck.logging.module;

import org.holodeck.common.store.JpaUtil;

/**
 * @author Hamid Ben Malek
 */
public class DbStore extends JpaUtil
{
   public DbStore() {}
   public DbStore(String pUnit) { super(pUnit); }

}