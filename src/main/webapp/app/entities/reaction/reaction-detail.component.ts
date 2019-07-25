import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IReaction } from 'app/shared/model/reaction.model';

@Component({
  selector: 'dbt-reaction-detail',
  templateUrl: './reaction-detail.component.html'
})
export class ReactionDetailComponent implements OnInit {
  reaction: IReaction;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ reaction }) => {
      this.reaction = reaction;
    });
  }

  byteSize(field) {
    return this.dataUtils.byteSize(field);
  }

  openFile(contentType, field) {
    return this.dataUtils.openFile(contentType, field);
  }
  previousState() {
    window.history.back();
  }
}
