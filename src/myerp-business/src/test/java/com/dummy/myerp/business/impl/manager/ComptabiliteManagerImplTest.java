package com.dummy.myerp.business.impl.manager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.dummy.myerp.business.impl.TransactionManager;
import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.consumer.dao.contrat.DaoProxy;
import com.dummy.myerp.model.bean.comptabilite.*;
import com.dummy.myerp.technical.exception.NotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.dummy.myerp.technical.exception.FunctionalException;
import org.mockito.Mockito;

import static com.dummy.myerp.business.impl.AbstractBusinessManager.configure;


public class ComptabiliteManagerImplTest {

    private ComptabiliteManagerImpl manager = Mockito.spy(new ComptabiliteManagerImpl());
    private ComptabiliteDao comptabiliteDao = Mockito.mock(ComptabiliteDao.class);

@Before
public void init () {
    DaoProxy daoProxy = Mockito.mock(DaoProxy.class);
    Mockito.when(daoProxy.getComptabiliteDao()).thenReturn(comptabiliteDao);
    TransactionManager transactionManager = Mockito.mock(TransactionManager.class);
    configure(null, daoProxy, transactionManager);
}

    @Test
    public void checkEcritureComptableUnit() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        //new date = date du jour par défault
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.setReference("AC-2022/00001");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                 null, new BigDecimal(123),
                                                                                 null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                                                                                 null, null,
                                                                                 new BigDecimal(123)));
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test (expected = FunctionalException.class)
    public void checkEcritureComptableUnitDateRefKO() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        //new date = date du jour par défault
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.setReference("AC-2021/00001");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                null, null,
                new BigDecimal(123)));
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test (expected = FunctionalException.class)
    public void checkEcritureComptableUnitCodeRefKO() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        //new date = date du jour par défault
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        //Mauvaise référence "AB"
        vEcritureComptable.setReference("AB-2022/00001");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                null, null,
                new BigDecimal(123)));
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test (expected = FunctionalException.class)
    public void checkEcritureComptableUnitPasDebit() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        //new date = date du jour par défault
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.setReference("AC-2022/00001");
      //Nous avons uniquement une ligne crédit
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, null,
                new BigDecimal(123)));
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test (expected = FunctionalException.class)
    public void checkEcritureComptableUnitPasDebitDeuxCredit() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        //new date = date du jour par défault
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.setReference("AC-2022/00001");
        //Nous avons uniquement deux lignes crédit
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, null,
                new BigDecimal(123)));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                null, null,
                new BigDecimal(123)));
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test (expected = FunctionalException.class)
    public void checkEcritureComptableUnitPasDebitDeuxDebit() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        //new date = date du jour par défault
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.setReference("AC-2022/00001");
        //Nous avons uniquement deux lignes débit
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null,
                new BigDecimal(123),null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                null,
                new BigDecimal(123),null));
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitViolation() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitRG2() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                 null, new BigDecimal(123),
                                                                                 null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                                                                                 null, null,
                                                                                 new BigDecimal(1234)));
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitRG3() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                 null, new BigDecimal(123),
                                                                                 null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                 null, new BigDecimal(123),
                                                                                 null));
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }


    @Test
    public void addFirstReference () {
        EcritureComptable pEcritureComptable;
        pEcritureComptable = new EcritureComptable();
        pEcritureComptable.setDate(new GregorianCalendar(2022, Calendar.FEBRUARY, 1).getTime());
        pEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        Mockito.when(comptabiliteDao.getSequenceEcritureComptableByJournalAndAnnee("AC", 2022)).thenReturn(null);
        manager.addReference(pEcritureComptable);



        Assert.assertEquals("AC-2022/00001",pEcritureComptable.getReference());
    }

@Test
    public void addSecondReference () {
    EcritureComptable pEcritureComptable;
    pEcritureComptable = new EcritureComptable();
    pEcritureComptable.setDate(new GregorianCalendar(2022, Calendar.FEBRUARY, 1).getTime());
    pEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
    SequenceEcritureComptable sequenceEcritureComptable = new SequenceEcritureComptable(2022, 1);
    Mockito.when(comptabiliteDao.getSequenceEcritureComptableByJournalAndAnnee("AC", 2022)).thenReturn(sequenceEcritureComptable);
    manager.addReference(pEcritureComptable);

    Assert.assertEquals("AC-2022/00002",pEcritureComptable.getReference());
}

@Test
    public void checkEcritureComptable () {


}

@Test
    public void checkEcritureComptableContext () throws NotFoundException, FunctionalException {
    EcritureComptable ecritureComptable;
    ecritureComptable = new EcritureComptable();
    ecritureComptable.setReference("AC-2022/00002");
    ecritureComptable.setId(1);
    // La référence n'existe pas en base, première insertion
    Mockito.when(comptabiliteDao.getEcritureComptableByRef("AC-2022/00002")).thenThrow(new NotFoundException());
manager.checkEcritureComptableContext(ecritureComptable);
}

    @Test (expected = FunctionalException.class)
    public void checkEcritureComptableContextEdition () throws NotFoundException, FunctionalException {
        EcritureComptable ecritureComptable;
        ecritureComptable = new EcritureComptable();
        ecritureComptable.setReference("AC-2022/00002");
        ecritureComptable.setId(1);

        EcritureComptable ecritureComptableExistante;
        ecritureComptableExistante = new EcritureComptable();
        ecritureComptableExistante.setReference("AC-2022/00002");
        ecritureComptableExistante.setId(2);
        // La référence existe en base

        Mockito.when(comptabiliteDao.getEcritureComptableByRef("AC-2022/00002")).thenReturn(ecritureComptableExistante);
        manager.checkEcritureComptableContext(ecritureComptable);
    }



    @Test
    public void checkEcritureComptableContextRefVide () throws FunctionalException {
        EcritureComptable ecritureComptable;
        ecritureComptable = new EcritureComptable();
        ecritureComptable.setReference("");

        manager.checkEcritureComptableContext(ecritureComptable);
    }

    @Test (expected = FunctionalException.class)
    public void checkEcritureComptableContextEditonIdVide () throws NotFoundException, FunctionalException {
        EcritureComptable ecritureComptable;
        ecritureComptable = new EcritureComptable();
        ecritureComptable.setReference("AC-2022/00002");
        // La référence existe en base, je tente une insertion
        Mockito.when(comptabiliteDao.getEcritureComptableByRef("AC-2022/00002")).thenReturn(ecritureComptable);
        manager.checkEcritureComptableContext(ecritureComptable);
    }
}
