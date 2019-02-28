import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { IReaction } from 'app/shared/model/reaction.model';

import { VisEdges, VisNetworkData, VisNetworkOptions, VisNetworkService, VisNodes } from 'ngx-vis';
import { ReactionService } from './reaction.service';

class DebattonsNetworkData implements VisNetworkData {
    public nodes: VisNodes;
    public edges: VisEdges;
}

@Component({
    selector: 'dbt-reaction-detail',
    templateUrl: './reaction-detail.component.html',
    styleUrls: ['./reaction-detail.component.css']
})
export class ReactionDetailComponent implements OnInit, OnDestroy {
    reaction: IReaction;

    visNetwork = 'debattonsNetwork';
    visNetworkData: DebattonsNetworkData;
    visNetworkOptions: VisNetworkOptions;

    constructor(
        protected activatedRoute: ActivatedRoute,
        private router: Router,
        private visNetworkService: VisNetworkService,
        private reactionService: ReactionService
    ) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ reaction }) => {
            this.reaction = reaction;

            this.reactionService.loadChildren(reaction).subscribe(reactionWithChildren => {
                const nodes = new VisNodes([
                    {
                        id: reactionWithChildren.id,
                        label: reactionWithChildren.title,
                        title: reactionWithChildren.content,
                        x: 0,
                        y: 0,
                        color: { border: '#193357' },
                        shape: 'box'
                    }
                ]);
                const edges = new VisEdges([]);

                for (const childReaction of reactionWithChildren.childrenReactions) {
                    nodes.add({ id: childReaction.id, label: childReaction.title, title: childReaction.content, shape: 'box' });
                    edges.add({ from: reactionWithChildren.id, to: childReaction.id });
                }

                if (reactionWithChildren.parentReaction) {
                    const parentReaction = reactionWithChildren.parentReaction;
                    nodes.add({
                        id: parentReaction.id,
                        label: parentReaction.title,
                        title: parentReaction.content,
                        x: 0,
                        y: -50,
                        color: '#4793fc',
                        shape: 'box'
                    });
                    edges.add({ from: parentReaction.id, to: reactionWithChildren.id });
                }

                this.visNetworkData = {
                    nodes,
                    edges
                };
            });
        });

        this.visNetworkOptions = {
            edges: { arrows: 'to' },
            layout: { randomSeed: 1 }
        };
    }

    ngOnDestroy(): void {
        this.visNetworkService.off(this.visNetwork, 'click');
    }

    previousState() {
        window.history.back();
    }

    networkInitialized(): void {
        // now we can use the service to register on events
        this.visNetworkService.on(this.visNetwork, 'click');

        // open your console/dev tools to see the click params
        this.visNetworkService.click.subscribe((eventData: any[]) => {
            if (eventData[0] === this.visNetwork) {
                const reactionId = eventData[1].nodes[0];
                if (reactionId !== this.reaction.id) {
                    // [routerLink]="['/reaction', reaction.reactedTo[1].id]"
                    this.router.navigate(['/reaction/', reactionId, 'view']);
                }
            }
        });
        this.visNetworkService.fit(this.visNetwork);
    }
}
