import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { ClassificationComponentsPage, ClassificationDeleteDialog, ClassificationUpdatePage } from './classification.page-object';

const expect = chai.expect;

describe('Classification e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let classificationComponentsPage: ClassificationComponentsPage;
  let classificationUpdatePage: ClassificationUpdatePage;
  let classificationDeleteDialog: ClassificationDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Classifications', async () => {
    await navBarPage.goToEntity('classification');
    classificationComponentsPage = new ClassificationComponentsPage();
    await browser.wait(ec.visibilityOf(classificationComponentsPage.title), 5000);
    expect(await classificationComponentsPage.getTitle()).to.eq('gatewayApp.microserviceClassification.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(classificationComponentsPage.entities), ec.visibilityOf(classificationComponentsPage.noResult)),
      1000
    );
  });

  it('should load create Classification page', async () => {
    await classificationComponentsPage.clickOnCreateButton();
    classificationUpdatePage = new ClassificationUpdatePage();
    expect(await classificationUpdatePage.getPageTitle()).to.eq('gatewayApp.microserviceClassification.home.createOrEditLabel');
    await classificationUpdatePage.cancel();
  });

  it('should create and save Classifications', async () => {
    const nbButtonsBeforeCreate = await classificationComponentsPage.countDeleteButtons();

    await classificationComponentsPage.clickOnCreateButton();

    await promise.all([
      classificationUpdatePage.raunkierSelectLastOption(),
      classificationUpdatePage.cronquistSelectLastOption(),
      classificationUpdatePage.apg1SelectLastOption(),
      classificationUpdatePage.apg2SelectLastOption(),
      classificationUpdatePage.apg3SelectLastOption(),
      classificationUpdatePage.apg4SelectLastOption(),
    ]);

    await classificationUpdatePage.save();
    expect(await classificationUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await classificationComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last Classification', async () => {
    const nbButtonsBeforeDelete = await classificationComponentsPage.countDeleteButtons();
    await classificationComponentsPage.clickOnLastDeleteButton();

    classificationDeleteDialog = new ClassificationDeleteDialog();
    expect(await classificationDeleteDialog.getDialogTitle()).to.eq('gatewayApp.microserviceClassification.delete.question');
    await classificationDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(classificationComponentsPage.title), 5000);

    expect(await classificationComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
