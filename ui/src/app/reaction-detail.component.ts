import {Component, OnDestroy, OnInit} from "@angular/core";
import {Reaction} from "./reaction";
import {ReactionService} from "./reaction.service";
import {ActivatedRoute, ParamMap, Router} from "@angular/router";

import 'rxjs/add/operator/switchMap';
import {VisEdges, VisNetworkData, VisNetworkOptions, VisNetworkService, VisNodes} from "ngx-vis";


class DebattonsNetworkData implements VisNetworkData {
  public nodes: VisNodes;
  public edges: VisEdges;
}


@Component({
  selector: 'reaction-detail',
  templateUrl: './reaction-detail.component.html',
  styleUrls: ['./reaction-detail.component.css']
})
export class ReactionDetailComponent implements OnInit, OnDestroy {
  reaction: Reaction;

  visNetwork: string = 'debattonsNetwork';
  visNetworkData: DebattonsNetworkData;
  visNetworkOptions: VisNetworkOptions;

  constructor(private reactionService: ReactionService,
              private route: ActivatedRoute,
              private router: Router,
              private visNetworkService: VisNetworkService) {
  }

  ngOnInit(): void {
    this.route.paramMap
      .switchMap((params: ParamMap) => this.reactionService.findByIdLoadingDepth(params.get('id'), 1))
      .subscribe(reaction => {
        this.reaction = reaction;

        let nodes = new VisNodes([{
          id: reaction.id,
          label: reaction.title,
          title: reaction.content,
          x: 0, y: 0,
          color: {border: '#193357'},
          shape: 'box'
        }]);
        let edges = new VisEdges([]);

        for (let childReaction of reaction.reactedTo) {
          nodes.add({id: childReaction.id, label: childReaction.title, title: childReaction.content, shape: 'box'});
          edges.add({from: reaction.id, to: childReaction.id});
        }

        if (reaction.reactedFrom) {
          let reactedFrom = reaction.reactedFrom;
          nodes.add({
            id: reactedFrom.id,
            label: reactedFrom.title,
            title: reactedFrom.content,
            x: 0, y: -50,
            color: '#4793fc',
            shape: 'box'
          });
          edges.add({from: reactedFrom.id, to: reaction.id});
        }

        this.visNetworkData = {
          nodes: nodes,
          edges: edges
        };
      });

    this.visNetworkOptions = {
      edges: {arrows: 'to'},
      layout: {randomSeed: 1}
    };
  }

  ngOnDestroy(): void {
    this.visNetworkService.off(this.visNetwork, 'click');
  }

  public networkInitialized(): void {
    // now we can use the service to register on events
    this.visNetworkService.on(this.visNetwork, 'click');

    // open your console/dev tools to see the click params
    this.visNetworkService.click
      .subscribe((eventData: any[]) => {
        if (eventData[0] === this.visNetwork) {
          let reactionId = eventData[1].nodes[0];
          if (reactionId != this.reaction.id) {
            //[routerLink]="['/reaction', reaction.reactedTo[1].id]"
            this.router.navigate(['/reaction/', reactionId]);
          }
        }
      });
    this.visNetworkService.fit(this.visNetwork);
  }

  reactToThis() {
    this.router.navigate(['/new-reaction/', this.reaction.id]);
  }
}
