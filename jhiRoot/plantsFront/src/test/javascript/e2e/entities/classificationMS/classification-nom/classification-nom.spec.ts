import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  ClassificationNomComponentsPage,
  ClassificationNomDeleteDialog,
  ClassificationNomUpdatePage,
} from './classification-nom.page-object';

const expect = chai.expect;

describe('ClassificationNom e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let classificationNomComponentsPage: ClassificationNomComponentsPage;
  let classificationNomUpdatePage: ClassificationNomUpdatePage;
  let classificationNomDeleteDialog: ClassificationNomDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load ClassificationNoms', async () => {
    await navBarPage.goToEntity('classification-nom');
    classificationNomComponentsPage = new ClassificationNomComponentsPage();
    await browser.wait(ec.visibilityOf(classificationNomComponentsPage.title), 5000);
    expect(await classificationNomComponentsPage.getTitle()).to.eq('Classification Noms');
    await browser.wait(
      ec.or(ec.visibilityOf(classificationNomComponentsPage.entities), ec.visibilityOf(classificationNomComponentsPage.noResult)),
      1000
    );
  });

  it('should load create ClassificationNom page', async () => {
    await classificationNomComponentsPage.clickOnCreateButton();
    classificationNomUpdatePage = new ClassificationNomUpdatePage();
    expect(await classificationNomUpdatePage.getPageTitle()).to.eq('Create or edit a Classification Nom');
    await classificationNomUpdatePage.cancel();
  });

  it('should create and save ClassificationNoms', async () => {
    const nbButtonsBeforeCreate = await classificationNomComponentsPage.countDeleteButtons();

    await classificationNomComponentsPage.clickOnCreateButton();

    await promise.all([
      classificationNomUpdatePage.setNomFrInput('nomFr'),
      classificationNomUpdatePage.setNomLatinInput('nomLatin'),
      classificationNomUpdatePage.cronquistRankSelectLastOption(),
    ]);

    await classificationNomUpdatePage.save();
    expect(await classificationNomUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await classificationNomComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last ClassificationNom', async () => {
    const nbButtonsBeforeDelete = await classificationNomComponentsPage.countDeleteButtons();
    await classificationNomComponentsPage.clickOnLastDeleteButton();

    classificationNomDeleteDialog = new ClassificationNomDeleteDialog();
    expect(await classificationNomDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Classification Nom?');
    await classificationNomDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(classificationNomComponentsPage.title), 5000);

    expect(await classificationNomComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
