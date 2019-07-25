/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ReactionComponentsPage, ReactionDeleteDialog, ReactionUpdatePage } from './reaction.page-object';

const expect = chai.expect;

describe('Reaction e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let reactionUpdatePage: ReactionUpdatePage;
  let reactionComponentsPage: ReactionComponentsPage;
  let reactionDeleteDialog: ReactionDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Reactions', async () => {
    await navBarPage.goToEntity('reaction');
    reactionComponentsPage = new ReactionComponentsPage();
    await browser.wait(ec.visibilityOf(reactionComponentsPage.title), 5000);
    expect(await reactionComponentsPage.getTitle()).to.eq('debattonsApp.reaction.home.title');
  });

  it('should load create Reaction page', async () => {
    await reactionComponentsPage.clickOnCreateButton();
    reactionUpdatePage = new ReactionUpdatePage();
    expect(await reactionUpdatePage.getPageTitle()).to.eq('debattonsApp.reaction.home.createOrEditLabel');
    await reactionUpdatePage.cancel();
  });

  it('should create and save Reactions', async () => {
    const nbButtonsBeforeCreate = await reactionComponentsPage.countDeleteButtons();

    await reactionComponentsPage.clickOnCreateButton();
    await promise.all([
      reactionUpdatePage.setTitleInput('title'),
      reactionUpdatePage.setContentInput('content'),
      reactionUpdatePage.typeSelectLastOption(),
      reactionUpdatePage.setTypeLevelInput('5'),
      reactionUpdatePage.setSupportScoreInput('5'),
      reactionUpdatePage.setTotalChildrenCountInput('5'),
      reactionUpdatePage.creatorSelectLastOption(),
      reactionUpdatePage.parentReactionSelectLastOption()
    ]);
    expect(await reactionUpdatePage.getTitleInput()).to.eq('title', 'Expected Title value to be equals to title');
    expect(await reactionUpdatePage.getContentInput()).to.eq('content', 'Expected Content value to be equals to content');
    expect(await reactionUpdatePage.getTypeLevelInput()).to.eq('5', 'Expected typeLevel value to be equals to 5');
    expect(await reactionUpdatePage.getSupportScoreInput()).to.eq('5', 'Expected supportScore value to be equals to 5');
    expect(await reactionUpdatePage.getTotalChildrenCountInput()).to.eq('5', 'Expected totalChildrenCount value to be equals to 5');
    await reactionUpdatePage.save();
    expect(await reactionUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await reactionComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Reaction', async () => {
    const nbButtonsBeforeDelete = await reactionComponentsPage.countDeleteButtons();
    await reactionComponentsPage.clickOnLastDeleteButton();

    reactionDeleteDialog = new ReactionDeleteDialog();
    expect(await reactionDeleteDialog.getDialogTitle()).to.eq('debattonsApp.reaction.delete.question');
    await reactionDeleteDialog.clickOnConfirmButton();

    expect(await reactionComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
