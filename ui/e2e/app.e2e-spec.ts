import { DebattonsPage } from './app.po';

describe('debattons App', () => {
  let page: DebattonsPage;

  beforeEach(() => {
    page = new DebattonsPage();
  });

  it('should display welcome message', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('Welcome to Débattons !!!');
  });
});
