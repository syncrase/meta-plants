import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  ClassificationCronquistComponentsPage,
  ClassificationCronquistDeleteDialog,
  ClassificationCronquistUpdatePage,
} from './classification-cronquist.page-object';

const expect = chai.expect;

describe('ClassificationCronquist e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let classificationCronquistComponentsPage: ClassificationCronquistComponentsPage;
  let classificationCronquistUpdatePage: ClassificationCronquistUpdatePage;
  let classificationCronquistDeleteDialog: ClassificationCronquistDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load ClassificationCronquists', async () => {
    await navBarPage.goToEntity('classification-cronquist');
    classificationCronquistComponentsPage = new ClassificationCronquistComponentsPage();
    await browser.wait(ec.visibilityOf(classificationCronquistComponentsPage.title), 5000);
    expect(await classificationCronquistComponentsPage.getTitle()).to.eq('Classification Cronquists');
    await browser.wait(
      ec.or(
        ec.visibilityOf(classificationCronquistComponentsPage.entities),
        ec.visibilityOf(classificationCronquistComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create ClassificationCronquist page', async () => {
    await classificationCronquistComponentsPage.clickOnCreateButton();
    classificationCronquistUpdatePage = new ClassificationCronquistUpdatePage();
    expect(await classificationCronquistUpdatePage.getPageTitle()).to.eq('Create or edit a Classification Cronquist');
    await classificationCronquistUpdatePage.cancel();
  });

  it('should create and save ClassificationCronquists', async () => {
    const nbButtonsBeforeCreate = await classificationCronquistComponentsPage.countDeleteButtons();

    await classificationCronquistComponentsPage.clickOnCreateButton();

    await promise.all([
      classificationCronquistUpdatePage.setSuperRegneInput('superRegne'),
      classificationCronquistUpdatePage.setRegneInput('regne'),
      classificationCronquistUpdatePage.setSousRegneInput('sousRegne'),
      classificationCronquistUpdatePage.setRameauInput('rameau'),
      classificationCronquistUpdatePage.setInfraRegneInput('infraRegne'),
      classificationCronquistUpdatePage.setSuperEmbranchementInput('superEmbranchement'),
      classificationCronquistUpdatePage.setDivisionInput('division'),
      classificationCronquistUpdatePage.setSousEmbranchementInput('sousEmbranchement'),
      classificationCronquistUpdatePage.setInfraEmbranchementInput('infraEmbranchement'),
      classificationCronquistUpdatePage.setMicroEmbranchementInput('microEmbranchement'),
      classificationCronquistUpdatePage.setSuperClasseInput('superClasse'),
      classificationCronquistUpdatePage.setClasseInput('classe'),
      classificationCronquistUpdatePage.setSousClasseInput('sousClasse'),
      classificationCronquistUpdatePage.setInfraClasseInput('infraClasse'),
      classificationCronquistUpdatePage.setSuperOrdreInput('superOrdre'),
      classificationCronquistUpdatePage.setOrdreInput('ordre'),
      classificationCronquistUpdatePage.setSousOrdreInput('sousOrdre'),
      classificationCronquistUpdatePage.setInfraOrdreInput('infraOrdre'),
      classificationCronquistUpdatePage.setMicroOrdreInput('microOrdre'),
      classificationCronquistUpdatePage.setSuperFamilleInput('superFamille'),
      classificationCronquistUpdatePage.setFamilleInput('famille'),
      classificationCronquistUpdatePage.setSousFamilleInput('sousFamille'),
      classificationCronquistUpdatePage.setTribuInput('tribu'),
      classificationCronquistUpdatePage.setSousTribuInput('sousTribu'),
      classificationCronquistUpdatePage.setGenreInput('genre'),
      classificationCronquistUpdatePage.setSousGenreInput('sousGenre'),
      classificationCronquistUpdatePage.setSectionInput('section'),
      classificationCronquistUpdatePage.setSousSectionInput('sousSection'),
      classificationCronquistUpdatePage.setEspeceInput('espece'),
      classificationCronquistUpdatePage.setSousEspeceInput('sousEspece'),
      classificationCronquistUpdatePage.setVarieteInput('variete'),
      classificationCronquistUpdatePage.setSousVarieteInput('sousVariete'),
      classificationCronquistUpdatePage.setFormeInput('forme'),
      classificationCronquistUpdatePage.planteSelectLastOption(),
    ]);

    await classificationCronquistUpdatePage.save();
    expect(await classificationCronquistUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await classificationCronquistComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last ClassificationCronquist', async () => {
    const nbButtonsBeforeDelete = await classificationCronquistComponentsPage.countDeleteButtons();
    await classificationCronquistComponentsPage.clickOnLastDeleteButton();

    classificationCronquistDeleteDialog = new ClassificationCronquistDeleteDialog();
    expect(await classificationCronquistDeleteDialog.getDialogTitle()).to.eq(
      'Are you sure you want to delete this Classification Cronquist?'
    );
    await classificationCronquistDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(classificationCronquistComponentsPage.title), 5000);

    expect(await classificationCronquistComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
